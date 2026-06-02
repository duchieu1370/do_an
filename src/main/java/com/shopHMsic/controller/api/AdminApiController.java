package com.shopHMsic.controller.api;

import com.shopHMsic.dto.*;
import com.shopHMsic.entities.*;
import com.shopHMsic.repository.*;
import com.shopHMsic.service.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
public class AdminApiController {

    private final ProductService productService;
    private final CategoriesService categoriesService;
    private final SaleorderService saleorderService;
    private final SaleorderProductsService saleorderProductsService;
    private final ContactService contactService;
    private final SubcribeService subcribeService;
    private final UserService userService;
    private final RoleService roleService;

    private final TokenService tokenService;
    private final PasswordEncoder passwordEncoder;
    private final UserRoleService userRoleService;

    public AdminApiController(ProductService productService, CategoriesService categoriesService,
                              SaleorderService saleorderService, SaleorderProductsService saleorderProductsService,
                              ContactService contactService, SubcribeService subcribeService,
                              UserService userService, RoleService roleService, TokenService tokenService,
                              PasswordEncoder passwordEncoder, UserRoleService userRoleService) {
        this.productService = productService;
        this.categoriesService = categoriesService;
        this.saleorderService = saleorderService;
        this.saleorderProductsService = saleorderProductsService;
        this.contactService = contactService;
        this.subcribeService = subcribeService;
        this.userService = userService;
        this.roleService = roleService;
        this.tokenService = tokenService;
        this.passwordEncoder = passwordEncoder;
        this.userRoleService = userRoleService;
    }

    // --- PRODUCTS ---
    @GetMapping("/products")
    public ResponseEntity<PagerData<Product>> getProducts(
            @RequestParam(value = "keyword", required = false) String keyword,
            @RequestParam(value = "categoryId", required = false) Integer categoryId,
            @RequestParam(value = "page", defaultValue = "1") int page) {
        ProductSearchModel searchModel = new ProductSearchModel();
        searchModel.keyword = keyword;
        searchModel.categoryId = categoryId;
        searchModel.setPage(page);
        return ResponseEntity.ok(productService.search(searchModel));
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
    public ResponseEntity<Map<String, Object>> saveProduct(
            @ModelAttribute Product product,
            @RequestParam(value = "productAvatar", required = false) MultipartFile productAvatar,
            @RequestParam(value = "productPictures", required = false) MultipartFile[] productPictures) throws Exception {
        Map<String, Object> result = new HashMap<>();
        
        if (product.getId() == null || product.getId() <= 0) {
            Product added = productService.add(product, productAvatar, productPictures);
            productService.saveOrUpdate(added);
            result.put("message", "Thêm mới sản phẩm thành công!");
        } else {
            productService.update(product, productAvatar, productPictures);
            result.put("message", "Cập nhật sản phẩm thành công!");
        }
        result.put("code", 200);
        return ResponseEntity.ok(result);
    }

    @DeleteMapping("/products/{id}")
    public ResponseEntity<Map<String, Object>> deleteProduct(@PathVariable("id") int id) {
        productService.deleteById(id);
        Map<String, Object> result = new HashMap<>();
        result.put("code", 200);
        result.put("message", "Đã xóa sản phẩm thành công!");
        return ResponseEntity.ok(result);
    }

    // --- CATEGORIES ---
    @GetMapping("/categories")
    public ResponseEntity<List<Categories>> getCategories() {
        return ResponseEntity.ok(categoriesService.findAll());
    }

    @PostMapping("/categories")
    public ResponseEntity<Map<String, Object>> saveCategory(@RequestBody Categories category) {
        categoriesService.saveOrUpdate(category);
        Map<String, Object> result = new HashMap<>();
        result.put("code", 200);
        result.put("message", "Lưu danh mục thành công!");
        return ResponseEntity.ok(result);
    }

    @DeleteMapping("/categories/{id}")
    public ResponseEntity<Map<String, Object>> deleteCategory(@PathVariable("id") int id) {
        categoriesService.deleteById(id);
        Map<String, Object> result = new HashMap<>();
        result.put("code", 200);
        result.put("message", "Đã xóa danh mục thành công!");
        return ResponseEntity.ok(result);
    }

    // --- ORDERS ---
    @GetMapping("/orders")
    public ResponseEntity<PagerData<Saleorder>> getOrders(
            @RequestParam(value = "keyword", required = false) String keyword,
            @RequestParam(value = "page", defaultValue = "1") int page) {
        OrderSearchModel searchModel = new OrderSearchModel();
        searchModel.keyword = keyword;
        searchModel.setPage(page);
        return ResponseEntity.ok(saleorderService.search(searchModel));
    }

    @GetMapping("/orders/details")
    public ResponseEntity<PagerData<SaleorderProducts>> getOrderDetails(
            @RequestParam(value = "keyword", required = false) String keyword,
            @RequestParam(value = "page", defaultValue = "1") int page) {
        OrderSearchModel searchModel = new OrderSearchModel();
        searchModel.keyword = keyword;
        searchModel.setPage(page);
        return ResponseEntity.ok(saleorderProductsService.search(searchModel));
    }

    @DeleteMapping("/orders/details/{id}")
    public ResponseEntity<Map<String, Object>> deleteOrderDetail(@PathVariable("id") int id) {
        saleorderProductsService.deleteById(id);
        Map<String, Object> result = new HashMap<>();
        result.put("code", 200);
        result.put("message", "Đã xóa chi tiết đơn hàng!");
        return ResponseEntity.ok(result);
    }

    // --- CONTACTS ---
    @GetMapping("/contacts")
    public ResponseEntity<PagerData<com.shopHMsic.entities.Contact>> getContacts(
            @RequestParam(value = "keyword", required = false) String keyword,
            @RequestParam(value = "page", defaultValue = "1") int page) {
        ContactSearchModel searchModel = new ContactSearchModel();
        searchModel.keyword = keyword;
        searchModel.setPage(page);
        return ResponseEntity.ok(contactService.search(searchModel));
    }

    @DeleteMapping("/contacts/{id}")
    public ResponseEntity<Map<String, Object>> deleteContact(@PathVariable("id") int id) {
        contactService.deleteById(id);
        Map<String, Object> result = new HashMap<>();
        result.put("code", 200);
        result.put("message", "Đã xóa thư liên hệ!");
        return ResponseEntity.ok(result);
    }

    // --- SUBSCRIBES ---
    @GetMapping("/subscribes")
    public ResponseEntity<PagerData<Subcribe>> getSubscribes(
            @RequestParam(value = "keyword", required = false) String keyword,
            @RequestParam(value = "page", defaultValue = "1") int page) {
        SubcribeSearchModel searchModel = new SubcribeSearchModel();
        searchModel.keyword = keyword;
        searchModel.setPage(page);
        return ResponseEntity.ok(subcribeService.search(searchModel));
    }

    @DeleteMapping("/subscribes/{id}")
    public ResponseEntity<Map<String, Object>> deleteSubscribe(@PathVariable("id") int id) {
        subcribeService.deleteById(id);
        Map<String, Object> result = new HashMap<>();
        result.put("code", 200);
        result.put("message", "Đã xóa email đăng ký!");
        return ResponseEntity.ok(result);
    }

    // --- USERS ---
    @PostMapping("/users/search-list")
    public ResponseEntity<?> getUsers(
            @RequestBody UserAdminSearchDto dto) {
        if (dto.getSize() == null) {
            dto.setSize(10);
        }
        if (dto.getPage() == null) {
            dto.setPage(1);
        }
        Pageable pageable = PageRequest.of(dto.getPage() - 1, dto.getSize());
        Page<User> responses = userRoleService.getListUser(dto, pageable);
        return ResponseEntity.ok()
                .header("X-Total-Count", String.valueOf(responses.getTotalElements()))
                .body(BaseResponse.<List<?>>builder()
                        .data(responses.getContent())
                        .total(responses.getTotalElements())
                        .build());
    }

    @PostMapping("/users")
    @Transactional
    public ResponseEntity<Map<String, Object>> createUser(@RequestBody Map<String, Object> body) {
        Map<String, Object> response = new HashMap<>();
        String username = (String) body.get("username");
        String password = (String) body.get("password");
        String email = (String) body.get("email");
        String phone = (String) body.get("phone");
        String address = (String) body.get("address");
        List<Integer> roleIds = (List<Integer>) body.get("roleIds");

        if (username == null || username.trim().length() < 8) {
            response.put("code", 400);
            response.put("message", "Tài khoản phải chứa tối thiểu 8 ký tự!");
            return ResponseEntity.badRequest().body(response);
        }
        if (password == null || !password.matches("^(?=.*[A-Z])(?=.*\\d)(?=.*[!@#$%^&*(),.?\":{}|<>]).{8,}$")) {
            response.put("code", 400);
            response.put("message", "Mật khẩu phải chứa tối thiểu 8 ký tự, bao gồm ít nhất 1 chữ hoa, 1 chữ số và 1 ký tự đặc biệt!");
            return ResponseEntity.badRequest().body(response);
        }
        if (email == null || !email.matches("^[a-zA-Z0-9._%+-]+@gmail\\.com$")) {
            response.put("code", 400);
            response.put("message", "Địa chỉ email phải đúng định dạng @gmail.com!");
            return ResponseEntity.badRequest().body(response);
        }

        // Check unique constraints
        if (!userService.findByUserNameRegister(username.trim()).isEmpty()) {
            response.put("code", 400);
            response.put("message", "Tên đăng nhập đã tồn tại!");
            return ResponseEntity.badRequest().body(response);
        }
        if (!userService.findByEmailRegister(email.trim()).isEmpty()) {
            response.put("code", 400);
            response.put("message", "Email đã tồn tại!");
            return ResponseEntity.badRequest().body(response);
        }

        User user = new User();
        user.setUsername(username.trim());
        user.setPassword(passwordEncoder.encode(password));
        user.setEmail(email.trim());
        user.setPhone(phone);
        user.setAddress(address);
        user.setStatus(true);
        user.setCreatedDate(new java.util.Date());

        User savedUser = userService.saveOrUpdate(user);

        if (roleIds != null) {
            for (Integer roleId : roleIds) {
                Role role = roleService.getById(roleId);
                if (role != null) {
                    savedUser.addRoles(role);
                    roleService.saveOrUpdate(role);
                }
            }
        }
        userService.saveOrUpdate(savedUser);

        response.put("code", 200);
        response.put("message", "Thêm người dùng thành công!");
        return ResponseEntity.ok(response);
    }

    @PutMapping("/users/{id}")
    @Transactional
    public ResponseEntity<Map<String, Object>> updateUser(
            @PathVariable("id") int id,
            @RequestBody Map<String, Object> body) {
        Map<String, Object> response = new HashMap<>();
        User user = userService.getById(id);
        if (user == null) {
            response.put("code", 404);
            response.put("message", "Không tìm thấy người dùng!");
            return ResponseEntity.status(404).body(response);
        }

        String username = (String) body.get("username");
        String password = (String) body.get("password");
        String email = (String) body.get("email");
        String phone = (String) body.get("phone");
        String address = (String) body.get("address");
        List<Integer> roleIds = (List<Integer>) body.get("roleIds");

        if (username == null || username.trim().length() < 8) {
            response.put("code", 400);
            response.put("message", "Tài khoản phải chứa tối thiểu 8 ký tự!");
            return ResponseEntity.badRequest().body(response);
        }
        if (email == null || !email.matches("^[a-zA-Z0-9._%+-]+@gmail\\.com$")) {
            response.put("code", 400);
            response.put("message", "Địa chỉ email phải đúng định dạng @gmail.com!");
            return ResponseEntity.badRequest().body(response);
        }

        // Check unique constraints for username
        List<User> existUsers = userService.findByUserNameRegister(username.trim());
        if (!existUsers.isEmpty() && !existUsers.get(0).getId().equals(user.getId())) {
            response.put("code", 400);
            response.put("message", "Tên đăng nhập đã tồn tại ở tài khoản khác!");
            return ResponseEntity.badRequest().body(response);
        }

        // Check unique constraints for email
        List<User> existEmails = userService.findByEmailRegister(email.trim());
        if (!existEmails.isEmpty() && !existEmails.get(0).getId().equals(user.getId())) {
            response.put("code", 400);
            response.put("message", "Email đã tồn tại ở tài khoản khác!");
            return ResponseEntity.badRequest().body(response);
        }

        user.setUsername(username.trim());
        user.setEmail(email.trim());
        user.setPhone(phone);
        user.setAddress(address);
        user.setUpdatedDate(new java.util.Date());

        // Update password if provided
        if (password != null && !password.trim().isEmpty()) {
            if (!password.matches("^(?=.*[A-Z])(?=.*\\d)(?=.*[!@#$%^&*(),.?\":{}|<>]).{8,}$")) {
                response.put("code", 400);
                response.put("message", "Mật khẩu mới phải chứa tối thiểu 8 ký tự, bao gồm ít nhất 1 chữ hoa, 1 chữ số và 1 ký tự đặc biệt!");
                return ResponseEntity.badRequest().body(response);
            }
            user.setPassword(passwordEncoder.encode(password));
        }

        // Update roles
        java.util.Set<Role> currentRoles = new java.util.HashSet<>(user.getRoles());
        for (Role r : currentRoles) {
            user.deleteRoles(r);
            roleService.saveOrUpdate(r);
        }

        if (roleIds != null) {
            for (Integer roleId : roleIds) {
                Role role = roleService.getById(roleId);
                if (role != null) {
                    user.addRoles(role);
                    roleService.saveOrUpdate(role);
                }
            }
        }

        userService.saveOrUpdate(user);

        response.put("code", 200);
        response.put("message", "Cập nhật người dùng thành công!");
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/users/{id}")
    @Transactional
    public ResponseEntity<Map<String, Object>> deleteUser(@PathVariable("id") int id) {
        Map<String, Object> response = new HashMap<>();
        User user = userService.getById(id);
        if (user == null) {
            response.put("code", 404);
            response.put("message", "Không tìm thấy người dùng!");
            return ResponseEntity.status(404).body(response);
        }

        // Delete user's tokens first to avoid FK constraint violation
        tokenService.deleteByUser(user);

        // Remove relationship with Roles to clear the joint table TBL_USERS_ROLES
        java.util.Set<Role> currentRoles = new java.util.HashSet<>(user.getRoles());
        for (Role r : currentRoles) {
            user.deleteRoles(r);
            roleService.saveOrUpdate(r);
        }

        // Delete user
        userService.deleteById(id);

        response.put("code", 200);
        response.put("message", "Đã xóa người dùng thành công!");
        return ResponseEntity.ok(response);
    }

    // --- ROLES ---
    @GetMapping("/roles")
    public ResponseEntity<List<Role>> getRoles() {
        return ResponseEntity.ok(roleService.findAll());
    }

    @GetMapping("/roles/{id}")
    public ResponseEntity<Role> getRoleById(@PathVariable("id") int id) {
        Role role = roleService.getById(id);
        if (role != null) {
            return ResponseEntity.ok(role);
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping("/roles")
    public ResponseEntity<Map<String, Object>> saveRole(@RequestBody Role role) {
        Map<String, Object> result = new HashMap<>();
        if (role.getId() == null || role.getId() <= 0) {
            role.setCreatedDate(new java.util.Date());
            roleService.saveOrUpdate(role);
            result.put("message", "Thêm mới nhóm quyền thành công!");
        } else {
            role.setUpdatedDate(new java.util.Date());
            roleService.saveOrUpdate(role);
            result.put("message", "Cập nhật nhóm quyền thành công!");
        }
        result.put("code", 200);
        return ResponseEntity.ok(result);
    }

    @DeleteMapping("/roles/{id}")
    public ResponseEntity<Map<String, Object>> deleteRole(@PathVariable("id") int id) {
        roleService.deleteById(id);
        Map<String, Object> result = new HashMap<>();
        result.put("code", 200);
        result.put("message", "Đã xóa nhóm quyền thành công!");
        return ResponseEntity.ok(result);
    }
}
