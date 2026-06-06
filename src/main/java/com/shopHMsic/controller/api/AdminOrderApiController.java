package com.shopHMsic.controller.api;

import com.shopHMsic.dto.BaseResponse;
import com.shopHMsic.dto.GetResponseDTO;
import com.shopHMsic.dto.OrderSearchModel;
import com.shopHMsic.entities.Saleorder;
import com.shopHMsic.service.SaleorderService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
public class AdminOrderApiController {

    private final SaleorderService saleorderService;

    public AdminOrderApiController(SaleorderService saleorderService) {
        this.saleorderService = saleorderService;
    }

    @PostMapping("/orders/search-list")
    public ResponseEntity<?> getOrders(
            @RequestBody OrderSearchModel searchModel) {
        if (searchModel.getSize() == null) {
            searchModel.setSize(20);
        }
        if (searchModel.getPage() == null) {
            searchModel.setPage(1);
        }
        Pageable pageable = PageRequest.of(searchModel.getPage() - 1, searchModel.getSize());
        Page<Saleorder> responses = saleorderService.getListOrder(searchModel, pageable);
        return ResponseEntity.ok()
                .header("X-Total-Count", String.valueOf(responses.getTotalElements()))
                .body(BaseResponse.<List<?>>builder()
                        .data(responses.getContent())
                        .total(responses.getTotalElements())
                        .build());
    }

    @GetMapping("/orders/{id}")
    public ResponseEntity<Saleorder> getOrderById(@PathVariable("id") int id) {
        Saleorder order = saleorderService.getById(id);
        if (order != null) {
            return ResponseEntity.ok(order);
        }
        return ResponseEntity.notFound().build();
    }

    @PutMapping("/orders/{id}/status")
    public ResponseEntity<?> updateOrderStatus(
            @PathVariable("id") int id,
            @RequestBody Map<String, Object> body) throws Exception {
        Integer orderStatus = (Integer) body.get("orderStatus");
        String reason = (String) body.get("reason");
        if (orderStatus == null) {
            GetResponseDTO responseDTO = GetResponseDTO.builder()
                    .code(400)
                    .message("Trạng thái không được để trống!")
                    .build();
            return ResponseEntity.badRequest().body(responseDTO);
        }
        saleorderService.updateOrderStatus(id, orderStatus, reason);
        GetResponseDTO responseDTO = GetResponseDTO.builder()
                    .code(200)
                    .message("Cập nhật trạng thái đơn hàng thành công!")
                    .build();
        return ResponseEntity.ok().body(responseDTO);
    }

    @DeleteMapping("/orders/{id}")
    public ResponseEntity<Map<String, Object>> deleteOrder(@PathVariable("id") int id) throws Exception {
        saleorderService.deleteOrder(id);
        Map<String, Object> result = new HashMap<>();
        result.put("code", 200);
        result.put("message", "Đã xóa đơn hàng thành công!");
        return ResponseEntity.ok(result);
    }
}
