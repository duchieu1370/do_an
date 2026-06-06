package com.shopHMsic.controller.api;

import com.shopHMsic.dto.BaseResponse;
import com.shopHMsic.dto.GetResponseDTO;
import com.shopHMsic.dto.ProductSearchModel;
import com.shopHMsic.entities.Product;
import com.shopHMsic.exception.EntityValidationException;
import com.shopHMsic.service.ProductService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
public class AdminProductApiController {

    private final ProductService productService;

    public AdminProductApiController(ProductService productService) {
        this.productService = productService;
    }

    // --- PRODUCTS ---
    @PostMapping("/products/search-list")
    public ResponseEntity<?> getProducts(
            @RequestBody ProductSearchModel searchModel) {
        if (searchModel.getSize() == null) {
            searchModel.setSize(20);
        }
        if (searchModel.getPage() == null) {
            searchModel.setPage(1);
        }
        Pageable pageable = PageRequest.of(searchModel.getPage() - 1, searchModel.getSize());
        Page<Product> responses = productService.getListProduct(searchModel, pageable);
        return ResponseEntity.ok()
                .header("X-Total-Count", String.valueOf(responses.getTotalElements()))
                .body(BaseResponse.<List<?>>builder()
                        .data(responses.getContent())
                        .total(responses.getTotalElements())
                        .build());
    }

    @GetMapping("/products/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable("id") int id) {
        Product product = productService.getById(id);
        if (product != null) {
            return ResponseEntity.ok(product);
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping("/products")
    public ResponseEntity<?> addProduct(
            @ModelAttribute Product product,
            @RequestParam(value = "productAvatar", required = false) MultipartFile productAvatar,
            @RequestParam(value = "productPictures", required = false) MultipartFile[] productPictures) throws Exception {
        productService.add(product, productAvatar, productPictures);
        GetResponseDTO responseDTO = GetResponseDTO.builder()
                .code(200)
                .message("Thêm mới sản phẩm thành công!")
                .build();
        return ResponseEntity.ok().body(responseDTO);
    }

    @PutMapping("/products/{id}")
    public ResponseEntity<?> updateProduct(
            @PathVariable("id") int id,
            @ModelAttribute Product product,
            @RequestParam(value = "productAvatar", required = false) MultipartFile productAvatar,
            @RequestParam(value = "productPictures", required = false) MultipartFile[] productPictures) throws Exception {
        product.setId(id);
        productService.update(product, productAvatar, productPictures);
        GetResponseDTO responseDTO = GetResponseDTO.builder()
                .code(200)
                .message("Cập nhật sản phẩm thành công!")
                .build();
        return ResponseEntity.ok().body(responseDTO);
    }

    @DeleteMapping("/products/{id}")
    public ResponseEntity<Map<String, Object>> deleteProduct(@PathVariable("id") int id) throws EntityValidationException {
        productService.deleteById(id);
        Map<String, Object> result = new HashMap<>();
        result.put("code", 200);
        result.put("message", "Đã xóa sản phẩm thành công!");
        return ResponseEntity.ok(result);
    }

    @PutMapping("/products/{id}/status")
    public ResponseEntity<?> updateProductStatus(
            @PathVariable("id") int id,
            @RequestBody Map<String, Object> body) throws Exception {
        Boolean status = (Boolean) body.get("status");
        if (status == null) {
            GetResponseDTO responseDTO = GetResponseDTO.builder()
                    .code(400)
                    .message("Trạng thái không được để trống!")
                    .build();
            return ResponseEntity.badRequest().body(responseDTO);
        }
        productService.updateProductStatus(id, status);
        GetResponseDTO responseDTO = GetResponseDTO.builder()
                .code(200)
                .message("Cập nhật trạng thái sản phẩm thành công!")
                .build();
        return ResponseEntity.ok().body(responseDTO);
    }
}
