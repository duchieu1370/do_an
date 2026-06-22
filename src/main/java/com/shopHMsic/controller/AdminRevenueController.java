package com.shopHMsic.controller;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.PrintWriter;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@RequestMapping("/api/admin/revenue")
public class AdminRevenueController {

    @PersistenceContext
    private EntityManager entityManager;

    @GetMapping("/analytics")
    public ResponseEntity<?> getRevenueAnalytics() {
        Map<String, Object> response = new HashMap<>();

        try {
            // 1. Total & Expected Revenue
            BigDecimal realizedRevenue = getSingleValuedResult("SELECT SUM(total) FROM tbl_saleorder WHERE order_status = 2");
            BigDecimal expectedRevenue = getSingleValuedResult("SELECT SUM(total) FROM tbl_saleorder WHERE order_status IN (1, 2)");

            // 2. Orders count
            Long totalOrders = getSingleValuedLongResult("SELECT COUNT(*) FROM tbl_saleorder");
            Long paidOrders = getSingleValuedLongResult("SELECT COUNT(*) FROM tbl_saleorder WHERE order_status = 2");
            Long cancelledOrders = getSingleValuedLongResult("SELECT COUNT(*) FROM tbl_saleorder WHERE order_status IN (5, 6)");

            double cancellationRate = 0.0;
            if (totalOrders > 0) {
                cancellationRate = ((double) cancelledOrders / totalOrders) * 100.0;
            }

            // 3. Revenue by payment method
            Query methodQuery = entityManager.createNativeQuery(
                    "SELECT payment_method, SUM(total) FROM tbl_saleorder WHERE order_status = 2 GROUP BY payment_method"
            );
            List<Object[]> methodList = methodQuery.getResultList();
            List<Map<String, Object>> paymentMethodsData = new ArrayList<>();
            for (Object[] row : methodList) {
                Map<String, Object> map = new HashMap<>();
                map.put("method", row[0] != null ? row[0].toString() : "UNKNOWN");
                map.put("value", row[1] != null ? new BigDecimal(row[1].toString()) : BigDecimal.ZERO);
                paymentMethodsData.add(map);
            }

            // 4. Revenue by date (for charts)
            Query dateQuery = entityManager.createNativeQuery(
                    "SELECT TO_CHAR(created_date, 'YYYY-MM-DD') as order_date, SUM(total) " +
                    "FROM tbl_saleorder " +
                    "WHERE order_status = 2 " +
                    "GROUP BY TO_CHAR(created_date, 'YYYY-MM-DD') " +
                    "ORDER BY order_date ASC"
            );
            List<Object[]> dateList = dateQuery.getResultList();
            List<Map<String, Object>> dailyRevenueData = new ArrayList<>();
            for (Object[] row : dateList) {
                Map<String, Object> map = new HashMap<>();
                map.put("date", row[0].toString());
                map.put("revenue", row[1] != null ? new BigDecimal(row[1].toString()) : BigDecimal.ZERO);
                dailyRevenueData.add(map);
            }

            // 5. Top selling products
            Query productQuery = entityManager.createNativeQuery(
                    "SELECT p.title, SUM(op.quality) as qty " +
                    "FROM tbl_saleorder_products op " +
                    "JOIN tbl_products p ON op.product_id = p.id " +
                    "GROUP BY p.title " +
                    "ORDER BY qty DESC FETCH FIRST 5 ROWS ONLY"
            );
            List<Object[]> productList = productQuery.getResultList();
            List<Map<String, Object>> topProductsData = new ArrayList<>();
            for (Object[] row : productList) {
                Map<String, Object> map = new HashMap<>();
                map.put("productName", row[0].toString());
                map.put("quantity", row[1] != null ? Integer.parseInt(row[1].toString()) : 0);
                topProductsData.add(map);
            }

            response.put("code", 200);
            response.put("realizedRevenue", realizedRevenue);
            response.put("expectedRevenue", expectedRevenue);
            response.put("totalOrders", totalOrders);
            response.put("paidOrders", paidOrders);
            response.put("cancelledOrders", cancelledOrders);
            response.put("cancellationRate", cancellationRate);
            response.put("paymentMethods", paymentMethodsData);
            response.put("dailyRevenue", dailyRevenueData);
            response.put("topProducts", topProductsData);

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            response.put("code", 500);
            response.put("message", "Lỗi phân tích doanh thu: " + e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }

    @GetMapping("/export")
    public void exportRevenueCsv(HttpServletResponse response) {
        try {
            response.setContentType("text/csv; charset=UTF-8");
            response.setHeader("Content-Disposition", "attachment; filename=\"revenue_report_" + 
                    new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date()) + ".csv\"");

            // Write BOM for Excel UTF-8 support
            PrintWriter writer = response.getWriter();
            writer.write('\ufeff'); 

            // Headers
            writer.println("Mã đơn hàng,Khách hàng,Số điện thoại,Email,Tổng tiền (VND),Phương thức,Trạng thái,Ngày tạo");

            Query query = entityManager.createNativeQuery(
                    "SELECT code, customer_name, customer_phone, customer_email, total, payment_method, order_status, created_date " +
                    "FROM tbl_saleorder " +
                    "ORDER BY id DESC"
            );
            List<Object[]> orders = query.getResultList();

            for (Object[] row : orders) {
                String code = row[0] != null ? row[0].toString() : "";
                String name = row[1] != null ? row[1].toString().replace(",", " ") : "";
                String phone = row[2] != null ? row[2].toString() : "";
                String email = row[3] != null ? row[3].toString() : "";
                String total = row[4] != null ? row[4].toString() : "0";
                String method = row[5] != null ? row[5].toString() : "";
                String statusNum = row[6] != null ? row[6].toString() : "1";
                String statusLabel = getStatusLabel(statusNum);
                String date = row[7] != null ? row[7].toString() : "";

                writer.println(String.format("%s,%s,%s,%s,%s,%s,%s,%s", 
                        code, name, phone, email, total, method, statusLabel, date));
            }

            writer.flush();
            writer.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String getStatusLabel(String statusNum) {
        switch (statusNum) {
            case "1": return "Chờ thanh toán";
            case "2": return "Đã thanh toán";
            case "3": return "Đang giao hàng";
            case "4": return "Đã giao hàng";
            case "5": return "Đã hủy";
            case "6": return "Trả hàng";
            default: return "Chờ xử lý";
        }
    }

    private BigDecimal getSingleValuedResult(String sql) {
        Query q = entityManager.createNativeQuery(sql);
        Object res = q.getSingleResult();
        return res != null ? new BigDecimal(res.toString()) : BigDecimal.ZERO;
    }

    private Long getSingleValuedLongResult(String sql) {
        Query q = entityManager.createNativeQuery(sql);
        Object res = q.getSingleResult();
        return res != null ? Long.parseLong(res.toString()) : 0L;
    }
}
