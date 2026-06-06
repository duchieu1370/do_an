package com.shopHMsic.service.impl;

import com.shopHMsic.entities.Saleorder;
import com.shopHMsic.entities.SaleorderProducts;
import com.shopHMsic.service.EmailService;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.concurrent.CompletableFuture;

@Service
public class EmailServiceImpl implements EmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String fromEmail;

    @Override
    public void sendOrderConfirmation(Saleorder order) {
        if (order.getCustomer_email() == null || order.getCustomer_email().trim().isEmpty()) {
            return;
        }

        CompletableFuture.runAsync(() -> {
            try {
                MimeMessage message = mailSender.createMimeMessage();
                MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

                helper.setFrom(fromEmail);
                helper.setTo(order.getCustomer_email());
                helper.setSubject("[HMsic Shop] Xác nhận đơn hàng thành công - Mã đơn hàng: " + order.getCode());

                String htmlContent = buildOrderEmailTemplate(order);
                helper.setText(htmlContent, true);

                mailSender.send(message);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    private String buildOrderEmailTemplate(Saleorder order) {
        NumberFormat nf = NumberFormat.getNumberInstance(new Locale("vi", "VN"));
        
        StringBuilder productsTable = new StringBuilder();
        BigDecimal totalCalculated = BigDecimal.ZERO;

        for (SaleorderProducts op : order.getSaleOrderProducts()) {
            if (op.getProduct() == null) continue;
            
            String title = op.getProduct().getTitle();
            String color = op.getColor() != null ? op.getColor() : "N/A";
            int qty = op.getQuality() != null ? op.getQuality() : 1;
            BigDecimal price = op.getProduct().getPriceSale() != null ? op.getProduct().getPriceSale() : op.getProduct().getPrice();
            BigDecimal subtotal = price.multiply(BigDecimal.valueOf(qty));
            totalCalculated = totalCalculated.add(subtotal);

            // Xử lý link ảnh sản phẩm
            String avatarPath = op.getProduct().getAvatar();
            String imageUrl = "https://images.unsplash.com/photo-1542291026-7eec264c27ff?auto=format&fit=crop&w=80&q=80"; // Mặc định
            if (avatarPath != null && !avatarPath.trim().isEmpty()) {
                if (avatarPath.startsWith("http://") || avatarPath.startsWith("https://")) {
                    imageUrl = avatarPath;
                } else {
                    imageUrl = "http://localhost:8080/upload/" + avatarPath;
                }
            }

            productsTable.append("<tr style='border-bottom: 1px solid #eeeeee;'>")
                    .append("  <td style='padding: 12px; vertical-align: middle; width: 60px;'>")
                    .append("    <img src='").append(imageUrl).append("' alt='").append(title).append("' style='width: 50px; height: 50px; object-fit: cover; border-radius: 6px; border: 1px solid #e2e8f0;' />")
                    .append("  </td>")
                    .append("  <td style='padding: 12px; vertical-align: middle;'>")
                    .append("    <div style='font-weight: bold; color: #1e293b; font-size: 14px;'>").append(title).append("</div>")
                    .append("    <div style='color: #64748b; font-size: 12px; margin-top: 2px;'>Màu sắc: <strong>").append(color).append("</strong></div>")
                    .append("  </td>")
                    .append("  <td style='padding: 12px; vertical-align: middle; text-align: center; color: #334155;'>").append(qty).append("</td>")
                    .append("  <td style='padding: 12px; vertical-align: middle; text-align: right; color: #334155;'>").append(nf.format(price)).append(" đ</td>")
                    .append("  <td style='padding: 12px; vertical-align: middle; text-align: right; font-weight: bold; color: #0f172a;'>").append(nf.format(subtotal)).append(" đ</td>")
                    .append("</tr>");
        }

        BigDecimal finalTotal = order.getTotal() != null ? order.getTotal() : totalCalculated;

        return "<div style='font-family: \"Segoe UI\", Roboto, Helvetica, Arial, sans-serif; max-width: 600px; margin: 0 auto; border: 1px solid #e2e8f0; border-radius: 12px; overflow: hidden; box-shadow: 0 4px 6px -1px rgba(0, 0, 0, 0.1), 0 2px 4px -1px rgba(0, 0, 0, 0.06); background-color: #ffffff;'>" +
                "  <div style='background: linear-gradient(135deg, #1e3c72 0%, #2a5298 100%); padding: 35px 24px; text-align: center; color: #ffffff;'>" +
                "    <h1 style='margin: 0; font-size: 26px; font-weight: 800; letter-spacing: 1.5px; text-transform: uppercase;'>HMSIC SHOP</h1>" +
                "    <p style='margin: 10px 0 0; opacity: 0.9; font-size: 16px; font-weight: 500;'>Xác Nhận Đơn Hàng Thành Công</p>" +
                "  </div>" +
                "  <div style='padding: 30px 24px; color: #334155; line-height: 1.6;'>" +
                "    <p style='font-size: 15px; margin-top: 0;'>Xin chào <strong style='color: #0f172a;'>" + order.getCustomer_name() + "</strong>,</p>" +
                "    <p style='font-size: 15px;'>Cảm ơn bạn đã mua sắm tại <strong>HMSIC SHOP</strong>. Đơn hàng của bạn đã được tiếp nhận và đang được chuẩn bị để vận chuyển.</p>" +
                "    " +
                "    <div style='background-color: #f8fafc; padding: 18px; border-radius: 8px; margin: 24px 0; border: 1px solid #e2e8f0; border-left: 4px solid #3b82f6;'>" +
                "      <h4 style='margin: 0 0 12px 0; color: #1e3c72; font-size: 15px; text-transform: uppercase; letter-spacing: 0.5px;'>Thông tin giao hàng</h4>" +
                "      <p style='margin: 0 0 8px 0; font-size: 14px;'><strong>Mã đơn hàng:</strong> <strong style='color: #2563eb;'>" + order.getCode() + "</strong></p>" +
                "      <p style='margin: 0 0 8px 0; font-size: 14px;'><strong>Người nhận:</strong> " + order.getCustomer_name() + "</p>" +
                "      <p style='margin: 0 0 8px 0; font-size: 14px;'><strong>Số điện thoại:</strong> " + order.getCustomer_phone() + "</p>" +
                "      <p style='margin: 0 0 8px 0; font-size: 14px;'><strong>Địa chỉ:</strong> " + order.getCustomer_address() + "</p>" +
                "      <p style='margin: 0; font-size: 14px;'><strong>Email:</strong> " + order.getCustomer_email() + "</p>" +
                "    </div>" +
                "    " +
                "    <h3 style='border-bottom: 2px solid #e2e8f0; padding-bottom: 8px; color: #1e293b; margin: 30px 0 15px 0; font-size: 16px; text-transform: uppercase; letter-spacing: 0.5px;'>Chi tiết sản phẩm</h3>" +
                "    <table style='width: 100%; border-collapse: collapse; margin-bottom: 24px;'>" +
                "      <thead>" +
                "        <tr style='background-color: #f8fafc; border-bottom: 2px solid #e2e8f0;'>" +
                "          <th style='padding: 12px; text-align: left; font-weight: 700; color: #475569; font-size: 13px;' colspan='2'>Sản phẩm</th>" +
                "          <th style='padding: 12px; text-align: center; font-weight: 700; color: #475569; font-size: 13px; width: 40px;'>SL</th>" +
                "          <th style='padding: 12px; text-align: right; font-weight: 700; color: #475569; font-size: 13px; width: 100px;'>Đơn giá</th>" +
                "          <th style='padding: 12px; text-align: right; font-weight: 700; color: #475569; font-size: 13px; width: 110px;'>Thành tiền</th>" +
                "        </tr>" +
                "      </thead>" +
                "      <tbody>" +
                productsTable.toString() +
                "      </tbody>" +
                "    </table>" +
                "    " +
                "    <div style='text-align: right; margin-top: 20px; font-size: 15px; border-top: 2px solid #e2e8f0; padding-top: 15px;'>" +
                "      <p style='margin: 0;'><strong>Tổng thanh toán: </strong><span style='color: #ef4444; font-size: 22px; font-weight: 800; margin-left: 10px;'>" + nf.format(finalTotal) + " đ</span></p>" +
                "    </div>" +
                "    " +
                "    <hr style='border: 0; border-top: 1px solid #e2e8f0; margin: 30px 0;'>" +
                "    " +
                "    <p style='font-size: 13px; color: #64748b; text-align: center; margin: 0; font-style: italic;'>Nếu có bất kỳ thắc mắc nào về đơn hàng, vui lòng liên hệ hotline chăm sóc khách hàng hoặc trả lời trực tiếp email này.</p>" +
                "    <p style='font-size: 13px; color: #64748b; text-align: center; margin: 6px 0 0; font-weight: 500;'>Chúc bạn luôn có trải nghiệm tuyệt vời cùng HMSIC Shop!</p>" +
                "  </div>" +
                "  <div style='background-color: #f8fafc; padding: 20px; text-align: center; font-size: 12px; color: #94a3b8; border-top: 1px solid #e2e8f0;'>" +
                "    © " + java.time.Year.now().getValue() + " HMSIC Shop. All rights reserved." +
                "  </div>" +
                "</div>";
    }
}
