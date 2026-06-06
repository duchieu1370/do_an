package com.shopHMsic.controller;

import com.shopHMsic.config.KeyPairManager;
import com.shopHMsic.config.JwtUtil;
import com.shopHMsic.entities.Token;
import com.shopHMsic.entities.User;
import com.shopHMsic.service.TokenService;
import com.shopHMsic.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.crypto.Cipher;
import java.io.IOException;
import java.util.*;

@RestController
public class LoginController extends BaseController {

    @Autowired
    private TokenService tokenService;

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private KeyPairManager keyPairManager;

    @RequestMapping(value = {"/login"}, method = RequestMethod.GET)
    public ResponseEntity<Map<String, Object>> login() throws IOException {
        Map<String, Object> response = new HashMap<>();
        response.put("code", 200);
        response.put("message", "Login view is loaded on frontend React");
        return ResponseEntity.ok(response);
    }

    @PostMapping(value = "/logout")
    public ResponseEntity<Map<String, Object>> logout(@RequestHeader(value = "Authorization", required = false) String authHeader) {
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String jwt = authHeader.substring(7);
            Optional<Token> tokenOpt = tokenService.findByToken(jwt);
            if (tokenOpt.isPresent()) {
                Token token = tokenOpt.get();
                token.setRevoked(true);
                token.setUpdatedDate(new Date());
                tokenService.saveOrUpdate(token);
            }
        }
        Map<String, Object> response = new HashMap<>();
        response.put("code", 200);
        response.put("message", "Logged out successfully");
        return ResponseEntity.ok(response);
    }

    @GetMapping("/api/auth/public-key")
    public ResponseEntity<Map<String, Object>> getPublicKey() {
        Map<String, Object> response = new HashMap<>();
        response.put("code", 200);
        response.put("publicKey", keyPairManager.getPublicKey());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/api/auth/login")
    public ResponseEntity<Map<String, Object>> apiLogin(@RequestBody Map<String, String> loginRequest) {
        Map<String, Object> response = new HashMap<>();
        String username = loginRequest.get("username");
        String encryptedPassword = loginRequest.get("password");

        if (username == null || username.trim().isEmpty() || encryptedPassword == null || encryptedPassword.trim().isEmpty()) {
            response.put("code", 400);
            response.put("message", "Tài khoản và mật khẩu không được để trống!");
            return ResponseEntity.badRequest().body(response);
        }

        try {
            // Find user in DB
            List<User> users = userService.findByUserNameRegister(username.trim());
            if (users.isEmpty()) {
                response.put("code", 401);
                response.put("message", "Tài khoản không tồn tại!");
                return ResponseEntity.status(401).body(response);
            }
            User user = users.get(0);

            // Block inactive user
            if (user.getStatus() != null && !user.getStatus()) {
                response.put("code", 401);
                response.put("message", "Tài khoản của bạn đã bị khóa/tắt hiệu lực!");
                return ResponseEntity.status(401).body(response);
            }

            // Decrypt RSA password
            String decryptedPassword;
            try {
                byte[] encryptedBytes = Base64.getDecoder().decode(encryptedPassword.trim());
                Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
                cipher.init(Cipher.DECRYPT_MODE, keyPairManager.getPrivateKey());
                byte[] decryptedBytes = cipher.doFinal(encryptedBytes);
                decryptedPassword = new String(decryptedBytes, "UTF-8");
            } catch (Exception e) {
                response.put("code", 400);
                response.put("message", "Giải mã mật khẩu thất bại!");
                return ResponseEntity.badRequest().body(response);
            }

            // Verify password using BCrypt
            if (!passwordEncoder.matches(decryptedPassword, user.getPassword())) {
                response.put("code", 401);
                response.put("message", "Mật khẩu không chính xác!");
                return ResponseEntity.status(401).body(response);
            }

            // Generate JWT Token
            String tokenStr = JwtUtil.generateToken(user.getUsername());
            
            // Calculate Expiry Date (8 hours)
            Date now = new Date();
            Date expiryDate = new Date(now.getTime() + (8 * 60 * 60 * 1000));

            // Save token to DB
            Token token = new Token();
            token.setToken(tokenStr);
            token.setUser(user);
            token.setExpiryDate(expiryDate);
            token.setRevoked(false);
            token.setCreatedDate(now);
            tokenService.saveOrUpdate(token);

            // User Info mapping
            Map<String, Object> userData = new HashMap<>();
            userData.put("id", user.getId());
            userData.put("username", user.getUsername());
            userData.put("email", user.getEmail());
            
            List<String> roles = new ArrayList<>();
            user.getRoles().forEach(r -> roles.add(r.getName()));
            userData.put("roles", roles);

            response.put("code", 200);
            response.put("message", "Đăng nhập thành công!");
            response.put("token", tokenStr);
            response.put("user", userData);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("code", 500);
            response.put("message", "Đã xảy ra lỗi hệ thống: " + e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }
}
