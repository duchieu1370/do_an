package com.shopHMsic.controller.api;

import com.shopHMsic.dto.Cart;
import com.shopHMsic.dto.CartItem;
import com.shopHMsic.entities.Product;
import com.shopHMsic.entities.Saleorder;
import com.shopHMsic.entities.SaleorderProducts;
import com.shopHMsic.entities.User;
import com.shopHMsic.service.ProductService;
import com.shopHMsic.service.SaleorderService;
import com.shopHMsic.service.EmailService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/cart")
public class CartApiController {

    private final ProductService productService;
    private final SaleorderService saleOrderService;
    private final EmailService emailService;

    public CartApiController(ProductService productService, SaleorderService saleOrderService, EmailService emailService) {
        this.productService = productService;
        this.saleOrderService = saleOrderService;
        this.emailService = emailService;
    }

    private Cart getOrCreateCart(HttpSession session) {
        Cart cart = (Cart) session.getAttribute("cart");
        if (cart == null) {
            cart = new Cart();
            session.setAttribute("cart", cart);
        }
        return cart;
    }

    private void calculateTotalPrice(Cart cart) {
        List<CartItem> cartItems = cart.getCartItems();
        BigDecimal total = BigDecimal.ZERO;
        for (CartItem ci : cartItems) {
            total = total.add(ci.getPriceUnit().multiply(BigDecimal.valueOf(ci.getQuanlity())));
        }
        cart.setTotalPrice(total);
    }

    private int getTotalItems(Cart cart) {
        int total = 0;
        for (CartItem item : cart.getCartItems()) {
            total += item.getQuanlity();
        }
        return total;
    }

    @GetMapping
    public ResponseEntity<Cart> getCart(HttpSession session) {
        Cart cart = getOrCreateCart(session);
        return ResponseEntity.ok(cart);
    }

    @PostMapping("/add")
    public ResponseEntity<Map<String, Object>> addToCart(HttpSession session, @RequestBody CartItem cartItem) {
        Cart cart = getOrCreateCart(session);
        List<CartItem> cartItems = cart.getCartItems();

        boolean isExists = false;
        for (CartItem item : cartItems) {
            if (item.getProductId() == cartItem.getProductId() && java.util.Objects.equals(item.getColor(), cartItem.getColor())) {
                isExists = true;
                item.setQuanlity(item.getQuanlity() + cartItem.getQuanlity());
            }
        }

        if (!isExists) {
            Product productInDb = productService.getById(cartItem.getProductId());
            cartItem.setProductName(productInDb.getTitle());
            cartItem.setPriceUnit(productInDb.getPrice());
            cart.getCartItems().add(cartItem);
        }

        calculateTotalPrice(cart);
        int totalItems = getTotalItems(cart);
        session.setAttribute("totalItems", totalItems);

        Map<String, Object> jsonResult = new HashMap<>();
        jsonResult.put("code", 200);
        jsonResult.put("message", "Thêm vào giỏ hàng thành công!");
        jsonResult.put("totalItems", totalItems);
        jsonResult.put("cart", cart);

        return ResponseEntity.ok(jsonResult);
    }

    @PostMapping("/update-quantity")
    public ResponseEntity<Map<String, Object>> updateQuantity(HttpSession session, @RequestBody Map<String, Object> payload) {
        int productId = ((Number) payload.get("productId")).intValue();
        String action = (String) payload.get("action"); // "increase" or "decrease"
        String color = (String) payload.get("color");

        Cart cart = getOrCreateCart(session);
        List<CartItem> cartItems = cart.getCartItems();

        int currentProductQuality = 0;
        CartItem toRemove = null;

        for (CartItem item : cartItems) {
            if (item.getProductId() == productId && java.util.Objects.equals(item.getColor(), color)) {
                if ("increase".equalsIgnoreCase(action)) {
                    currentProductQuality = item.getQuanlity() + 1;
                    item.setQuanlity(currentProductQuality);
                } else if ("decrease".equalsIgnoreCase(action)) {
                    currentProductQuality = item.getQuanlity() - 1;
                    if (currentProductQuality <= 0) {
                        toRemove = item;
                    } else {
                        item.setQuanlity(currentProductQuality);
                    }
                }
                break;
            }
        }

        if (toRemove != null) {
            cartItems.remove(toRemove);
        }

        calculateTotalPrice(cart);
        int totalItems = getTotalItems(cart);
        session.setAttribute("totalItems", totalItems);

        Map<String, Object> jsonResult = new HashMap<>();
        jsonResult.put("code", 200);
        jsonResult.put("totalItems", totalItems);
        jsonResult.put("currentProductQuality", currentProductQuality);
        jsonResult.put("cart", cart);

        return ResponseEntity.ok(jsonResult);
    }

    @DeleteMapping("/remove/{productId}")
    public ResponseEntity<Map<String, Object>> removeProduct(
            HttpSession session, 
            @PathVariable("productId") int productId,
            @RequestParam(value = "color", required = false) String color) {
        Cart cart = getOrCreateCart(session);
        List<CartItem> cartItems = cart.getCartItems();

        CartItem toRemove = null;
        for (CartItem item : cartItems) {
            if (item.getProductId() == productId && java.util.Objects.equals(item.getColor(), color)) {
                toRemove = item;
                break;
            }
        }

        if (toRemove != null) {
            cartItems.remove(toRemove);
        }

        calculateTotalPrice(cart);
        int totalItems = getTotalItems(cart);
        session.setAttribute("totalItems", totalItems);

        Map<String, Object> jsonResult = new HashMap<>();
        jsonResult.put("code", 200);
        jsonResult.put("message", "Đã xóa sản phẩm khỏi giỏ hàng");
        jsonResult.put("totalItems", totalItems);
        jsonResult.put("cart", cart);

        return ResponseEntity.ok(jsonResult);
    }

    @PostMapping("/checkout")
    public ResponseEntity<Map<String, Object>> checkout(HttpSession session, @RequestBody Map<String, String> checkoutData) {
        Map<String, Object> jsonResult = new HashMap<>();
        
        Cart cart = (Cart) session.getAttribute("cart");
        if (cart == null || cart.getCartItems().isEmpty()) {
            jsonResult.put("code", 400);
            jsonResult.put("message", "Giỏ hàng trống!");
            return ResponseEntity.badRequest().body(jsonResult);
        }

        // Tạo hóa đơn
        Saleorder saleOrder = new Saleorder();
        
        // Kiểm tra xem khách hàng đã đăng nhập chưa để liên kết hóa đơn với tài khoản
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails) {
            User userLogined = (User) principal;
            saleOrder.setUser(userLogined);
        }

        // Luôn sử dụng thông tin khách hàng nhập trên form
        saleOrder.setCustomer_name(checkoutData.get("customer_name"));
        saleOrder.setCustomer_email(checkoutData.get("customer_email"));
        saleOrder.setCustomer_address(checkoutData.get("customer_address"));
        saleOrder.setCustomer_phone(checkoutData.get("customer_phone"));
        
        // Thiết lập tổng tiền hóa đơn
        saleOrder.setTotal(cart.getTotalPrice());
        saleOrder.setOrderStatus(1); // Chờ xác nhận
        saleOrder.setCode(String.valueOf(System.currentTimeMillis()));

        // Kết nối sản phẩm
        for (CartItem cartItem : cart.getCartItems()) {
            SaleorderProducts saleOrderProducts = new SaleorderProducts();
            saleOrderProducts.setProduct(productService.getById(cartItem.getProductId()));
            saleOrderProducts.setQuality(cartItem.getQuanlity());
            saleOrderProducts.setColor(cartItem.getColor());
            saleOrder.addSaleOrderProducts(saleOrderProducts);
        }

        // Lưu vào DB
        saleOrderService.saveOrUpdate(saleOrder);

        // Gửi email xác nhận đơn hàng bất đồng bộ
        try {
            emailService.sendOrderConfirmation(saleOrder);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Xóa giỏ hàng
        session.setAttribute("cart", null);
        session.setAttribute("totalItems", 0);

        jsonResult.put("code", 200);
        jsonResult.put("message", "Đặt hàng thành công!");
        jsonResult.put("orderCode", saleOrder.getCode());

        return ResponseEntity.ok(jsonResult);
    }
}
