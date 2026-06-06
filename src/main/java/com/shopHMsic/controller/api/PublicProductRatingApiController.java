package com.shopHMsic.controller.api;

import com.shopHMsic.dto.GetResponseDTO;
import com.shopHMsic.entities.Product;
import com.shopHMsic.entities.ProductRating;
import com.shopHMsic.entities.User;
import com.shopHMsic.service.ProductRatingService;
import com.shopHMsic.service.ProductService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/public/products")
public class PublicProductRatingApiController {

    private final ProductRatingService ratingService;
    private final ProductService productService;

    public PublicProductRatingApiController(ProductRatingService ratingService, ProductService productService) {
        this.ratingService = ratingService;
        this.productService = productService;
    }

    @GetMapping("/{productId}/ratings")
    public ResponseEntity<?> getRatings(@PathVariable("productId") int productId) {
        List<ProductRating> ratings = ratingService.getRatingsByProductId(productId);
        // Trả về danh sách đánh giá có status = true (đang hoạt động/hiển thị)
        List<Map<String, Object>> responses = ratings.stream()
                .filter(r -> r.getStatus() != Boolean.FALSE)
                .map(r -> {
                    Map<String, Object> map = new HashMap<>();
                    map.put("id", r.getId());
                    map.put("rating", r.getRating());
                    map.put("comment", r.getComment());
                    map.put("createdDate", r.getCreatedDate());
                    if (r.getUser() != null) {
                        map.put("username", r.getUser().getUsername());
                        map.put("userAvatar", r.getUser().getAvatar());
                    }
                    return map;
                }).toList();
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/{productId}/my-rating")
    public ResponseEntity<?> getMyRating(@PathVariable("productId") int productId) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (!(principal instanceof User)) {
            return ResponseEntity.ok(null);
        }

        User userLogined = (User) principal;
        ProductRating pr = ratingService.getUserRatingForProduct(productId, userLogined.getId());
        
        if (pr == null) {
            return ResponseEntity.ok(null);
        }

        Map<String, Object> response = new HashMap<>();
        response.put("id", pr.getId());
        response.put("rating", pr.getRating());
        response.put("comment", pr.getComment());
        response.put("createdDate", pr.getCreatedDate());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{productId}/ratings")
    public ResponseEntity<?> addOrUpdateRating(
            @PathVariable("productId") int productId,
            @RequestBody Map<String, Object> body) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (!(principal instanceof User)) {
            GetResponseDTO responseDTO = GetResponseDTO.builder()
                    .code(401)
                    .message("Bạn cần đăng nhập để thực hiện đánh giá!")
                    .build();
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(responseDTO);
        }

        User userLogined = (User) principal;
        Integer ratingValue = (Integer) body.get("rating");
        String comment = (String) body.get("comment");

        if (ratingValue == null || ratingValue < 0 || ratingValue > 5) {
            GetResponseDTO responseDTO = GetResponseDTO.builder()
                    .code(400)
                    .message("Đánh giá sao không hợp lệ (từ 0 đến 5 sao)!")
                    .build();
            return ResponseEntity.badRequest().body(responseDTO);
        }

        Product product = productService.getById(productId);
        if (product == null || product.getId() == null) {
            GetResponseDTO responseDTO = GetResponseDTO.builder()
                    .code(404)
                    .message("Không tìm thấy sản phẩm!")
                    .build();
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseDTO);
        }

        try {
            ProductRating pr = new ProductRating();
            pr.setProduct(product);
            pr.setUser(userLogined);
            pr.setRating(ratingValue);
            pr.setComment(comment);
            
            ratingService.saveOrUpdateRating(pr);

            GetResponseDTO responseDTO = GetResponseDTO.builder()
                    .code(200)
                    .message("Gửi đánh giá thành công!")
                    .build();
            return ResponseEntity.ok(responseDTO);
        } catch (Exception e) {
            GetResponseDTO responseDTO = GetResponseDTO.builder()
                    .code(400)
                    .message(e.getMessage())
                    .build();
            return ResponseEntity.badRequest().body(responseDTO);
        }
    }
}
