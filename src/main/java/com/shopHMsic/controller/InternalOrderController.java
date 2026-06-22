package com.shopHMsic.controller;

import com.shopHMsic.entities.Saleorder;
import com.shopHMsic.service.SaleorderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/internal")
public class InternalOrderController {

    private static final String INTERNAL_SECRET = "msic-internal-secret-token-key-123456";

    @Autowired
    private SaleorderService saleorderService;

    @PostMapping("/orders/{id}/paid")
    public ResponseEntity<?> markOrderAsPaid(
            @PathVariable("id") int id,
            @RequestHeader(value = "X-Internal-Token", required = false) String token,
            @RequestBody Map<String, Object> payload) {
        
        Map<String, Object> response = new HashMap<>();
        
        // Secure endpoint validation
        if (token == null || !token.equals(INTERNAL_SECRET)) {
            response.put("code", 401);
            response.put("message", "Unauthorized internal request");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }

        try {
            Saleorder order = saleorderService.getById(id);
            if (order == null) {
                response.put("code", 404);
                response.put("message", "Order not found");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }

            // Set orderStatus: 2 (Paid/Processing) or as defined in system
            // From SaleorderServiceImpl: updateOrderStatus takes status and reason
            saleorderService.updateOrderStatus(id, 2, "Payment confirmed automatically via Payment Service");

            response.put("code", 200);
            response.put("message", "Order marked as PAID successfully");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("code", 500);
            response.put("message", "Internal server error: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
}
