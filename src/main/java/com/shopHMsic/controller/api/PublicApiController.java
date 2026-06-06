package com.shopHMsic.controller.api;

import com.shopHMsic.dto.BaseResponse;
import com.shopHMsic.dto.GetResponseDTO;
import com.shopHMsic.dto.OrderSearchModel;
import com.shopHMsic.dto.ProductSearchModel;
import com.shopHMsic.entities.*;
import com.shopHMsic.service.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/public")
public class PublicApiController {

    private final ProductService productService;
    private final CategoriesService categoriesService;
    private final SubcribeService subcribeService;
    private final ContactService contactService;
    private final SaleorderService saleorderService;
    private final UserRoleService userRoleService;

    public PublicApiController(ProductService productService, CategoriesService categoriesService,
                               SubcribeService subcribeService, ContactService contactService,
                               SaleorderService saleorderService, UserRoleService userRoleService) {
        this.productService = productService;
        this.categoriesService = categoriesService;
        this.subcribeService = subcribeService;
        this.contactService = contactService;
        this.saleorderService = saleorderService;
        this.userRoleService = userRoleService;
    }

    @GetMapping("/categories")
    public ResponseEntity<List<Categories>> getCategories() {
        return ResponseEntity.ok(categoriesService.getAllCategories());
    }

    @PostMapping("/products")
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

    @GetMapping("/products/{seo}")
    public ResponseEntity<Product> getProductDetails(@PathVariable("seo") String seo) {
        Product product = productService.getBySeo(seo);
        if (product != null) {
            return ResponseEntity.ok(product);
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping("/subscribe")
    public ResponseEntity<Map<String, Object>> subscribe(@RequestBody Subcribe subcribe) {
        Map<String, Object> jsonResult = new HashMap<>();
        List<Subcribe> subcribes = subcribeService.checkEmailSubcribe(subcribe);
        if (subcribes.isEmpty()) {
            subcribeService.saveOrUpdate(subcribe);
            jsonResult.put("code", 200);
            jsonResult.put("message", "Cảm ơn, " + subcribe.getEmail() + " đã đăng kí thành công!");
            return ResponseEntity.ok(jsonResult);
        } else {
            jsonResult.put("code", 400);
            jsonResult.put("err", "Bạn chưa nhập email / Trùng email");
            return ResponseEntity.badRequest().body(jsonResult);
        }
    }

    @PostMapping("/contact")
    public ResponseEntity<Map<String, Object>> contact(@RequestBody Contact contact) {
        Map<String, Object> jsonResult = new HashMap<>();
        contactService.saveOrUpdate(contact);
        jsonResult.put("code", 200);
        jsonResult.put("message", "Cảm ơn " + contact.getName() + " đã gửi liên hệ!");
        return ResponseEntity.ok(jsonResult);
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody User user) throws Exception {
        userRoleService.register(user);
        GetResponseDTO responseDTO = GetResponseDTO.builder()
                .code(200)
                .message("Đăng ký tài khoản mới thành công")
                .build();
        return ResponseEntity.ok().body(responseDTO);
    }

    @PostMapping("/orders")
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
}
