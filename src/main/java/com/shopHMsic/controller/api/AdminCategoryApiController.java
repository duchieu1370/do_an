package com.shopHMsic.controller.api;

import com.shopHMsic.dto.BaseResponse;
import com.shopHMsic.dto.CategoriesDto;
import com.shopHMsic.dto.CategorySearchModel;
import com.shopHMsic.dto.GetResponseDTO;
import com.shopHMsic.entities.Categories;
import com.shopHMsic.service.CategoriesService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
public class AdminCategoryApiController {

    private final CategoriesService categoriesService;

    public AdminCategoryApiController(CategoriesService categoriesService) {
        this.categoriesService = categoriesService;
    }

    // --- CATEGORIES ---
    @PostMapping("/categories/search-list")
    public ResponseEntity<?> getCategories(
            @RequestBody CategorySearchModel searchModel) {
        if (searchModel.getSize() == null) {
            searchModel.setSize(20);
        }
        if (searchModel.getPage() == null) {
            searchModel.setPage(1);
        }
        Pageable pageable = PageRequest.of(searchModel.getPage() - 1, searchModel.getSize());
        Page<Categories> responses = categoriesService.getListCategory(searchModel, pageable);
        return ResponseEntity.ok()
                .header("X-Total-Count", String.valueOf(responses.getTotalElements()))
                .body(BaseResponse.<List<?>>builder()
                        .data(responses.getContent())
                        .total(responses.getTotalElements())
                        .build());
    }

    @PostMapping("/categories")
    public ResponseEntity<?> createCategory(@RequestBody CategoriesDto dto) throws Exception {
        categoriesService.createCategories(dto);
        GetResponseDTO responseDTO = GetResponseDTO.builder()
                .code(200)
                .message("Thêm mới danh mục thành công!")
                .build();
        return ResponseEntity.ok().body(responseDTO);
    }

    @PutMapping("/categories/{id}")
    public ResponseEntity<?> updateCategory(
            @PathVariable("id") int id,
            @RequestBody CategoriesDto dto) throws Exception {
        categoriesService.updateCategory(id, dto);
        GetResponseDTO responseDTO = GetResponseDTO.builder()
                .code(200)
                .message("Cập nhật danh mục thành công!")
                .build();
        return ResponseEntity.ok().body(responseDTO);
    }

    @DeleteMapping("/categories/{id}")
    public ResponseEntity<?> deleteCategory(@PathVariable("id") int id) throws Exception {
        categoriesService.deleteCategory(id);
        GetResponseDTO responseDTO = GetResponseDTO.builder()
                .code(200)
                .message("Đã xóa danh mục thành công!")
                .build();
        return ResponseEntity.ok().body(responseDTO);
    }

    @PutMapping("/categories/{id}/status")
    public ResponseEntity<?> updateCategoryStatus(
            @PathVariable("id") int id,
            @RequestBody java.util.Map<String, Object> body) throws Exception {
        Boolean status = (Boolean) body.get("status");
        if (status == null) {
            GetResponseDTO responseDTO = GetResponseDTO.builder()
                    .code(400)
                    .message("Trạng thái không được để trống!")
                    .build();
            return ResponseEntity.badRequest().body(responseDTO);
        }
        categoriesService.updateCategoryStatus(id, status);
        GetResponseDTO responseDTO = GetResponseDTO.builder()
                .code(200)
                .message("Cập nhật trạng thái danh mục thành công!")
                .build();
        return ResponseEntity.ok().body(responseDTO);
    }
}
