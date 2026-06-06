package com.shopHMsic.controller.api;

import com.shopHMsic.dto.*;
import com.shopHMsic.entities.*;
import com.shopHMsic.service.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
public class AdminUserApiController {

    private final UserService userService;
    private final RoleService roleService;
    private final UserRoleService userRoleService;
    private final TokenService tokenService;
    private final PasswordEncoder passwordEncoder;
    private final S3Service s3Service;

    public AdminUserApiController(UserService userService, RoleService roleService,
                                  UserRoleService userRoleService, TokenService tokenService,
                                  PasswordEncoder passwordEncoder, S3Service s3Service) {
        this.userService = userService;
        this.roleService = roleService;
        this.userRoleService = userRoleService;
        this.tokenService = tokenService;
        this.passwordEncoder = passwordEncoder;
        this.s3Service = s3Service;
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
        String avatar = (String) body.get("avatar");
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
        user.setAvatar(avatar);
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
        String avatar = (String) body.get("avatar");
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
        user.setAvatar(avatar);
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

    @PutMapping("/users/{id}/status")
    @Transactional
    public ResponseEntity<Map<String, Object>> updateUserStatus(
            @PathVariable("id") int id,
            @RequestBody Map<String, Object> body) {
        Map<String, Object> response = new HashMap<>();
        User user = userService.getById(id);
        if (user == null) {
            response.put("code", 404);
            response.put("message", "Không tìm thấy người dùng!");
            return ResponseEntity.status(404).body(response);
        }

        Boolean status = (Boolean) body.get("status");
        if (status == null) {
            response.put("code", 400);
            response.put("message", "Trạng thái không được để trống!");
            return ResponseEntity.badRequest().body(response);
        }

        // Prevent disabling current logged in user
        org.springframework.security.core.Authentication auth = org.springframework.security.core.context.SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.getName().equals(user.getUsername()) && !status) {
            response.put("code", 400);
            response.put("message", "Bạn không thể tự tắt hiệu lực tài khoản của chính mình!");
            return ResponseEntity.badRequest().body(response);
        }

        user.setStatus(status);
        user.setUpdatedDate(new java.util.Date());
        userService.saveOrUpdate(user);

        response.put("code", 200);
        response.put("message", "Cập nhật trạng thái người dùng thành công!");
        return ResponseEntity.ok(response);
    }

    @PostMapping("/users/upload-avatar")
    public ResponseEntity<Map<String, Object>> uploadAvatar(@RequestParam("file") MultipartFile file) {
        Map<String, Object> response = new HashMap<>();
        try {
            if (file == null || file.isEmpty()) {
                response.put("code", 400);
                response.put("message", "Tệp tin không được để trống!");
                return ResponseEntity.badRequest().body(response);
            }
            String url = s3Service.uploadFile(file, "user-avatars");
            response.put("code", 200);
            response.put("url", url);
            response.put("message", "Tải ảnh đại diện lên thành công!");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            e.printStackTrace();
            response.put("code", 500);
            response.put("message", "Lỗi hệ thống khi tải ảnh lên S3/MinIO: " + e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }
}
