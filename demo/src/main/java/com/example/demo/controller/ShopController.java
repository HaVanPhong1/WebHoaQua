package com.example.demo.controller;
import com.example.demo.entity.Product;
import com.example.demo.entity.Customer;
import com.example.demo.entity.ShopOrder;
import com.example.demo.entity.OrderItem;
import com.example.demo.entity.OrderStatus;
import com.example.demo.entity.UserAccount;
import com.example.demo.repository.CategoryRepository;
import com.example.demo.repository.ProductRepository;
import com.example.demo.repository.CustomerRepository;
import com.example.demo.repository.ShopOrderRepository;
import com.example.demo.repository.UserAccountRepository;

import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.security.Principal;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;

@Controller
public class ShopController {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final CustomerRepository customerRepository;
    private final ShopOrderRepository shopOrderRepository;
    private final UserAccountRepository userAccountRepository;

    public ShopController(ProductRepository productRepository, 
                          CategoryRepository categoryRepository,
                          CustomerRepository customerRepository,
                          ShopOrderRepository shopOrderRepository,
                          UserAccountRepository userAccountRepository) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
        this.customerRepository = customerRepository;
        this.shopOrderRepository = shopOrderRepository;
        this.userAccountRepository = userAccountRepository;
    }

    @GetMapping({"/", "/shop"})
    public String shop(@RequestParam(required = false) Long categoryId, Model model) {
        List<Product> products = productRepository.findAll();
        if (categoryId != null) {
            products = products.stream()
                    .filter(product -> product.getCategory() != null && categoryId.equals(product.getCategory().getId()))
                    .toList();
        }
        model.addAttribute("products", products);
        model.addAttribute("categories", categoryRepository.findAll());
        model.addAttribute("selectedCategoryId", categoryId);
        return "shop";
    }

    @PostMapping("/shop/cart/add")
    @ResponseBody
    @SuppressWarnings("unchecked")
    public ResponseEntity<?> addToCart(@RequestParam Long productId, HttpSession session) {
        Map<Long, Integer> cart = (Map<Long, Integer>) session.getAttribute("cart");
        if (cart == null) {
            cart = new HashMap<>();
            session.setAttribute("cart", cart);
        }
        cart.put(productId, cart.getOrDefault(productId, 0) + 1);
        int totalItems = cart.values().stream().mapToInt(Integer::intValue).sum();
        return ResponseEntity.ok(Map.of("success", true, "totalItems", totalItems));
    }

    @PostMapping("/shop/cart/update")
    @ResponseBody
    @SuppressWarnings("unchecked")
    public ResponseEntity<?> updateCart(@RequestParam Long productId, @RequestParam Integer quantity, HttpSession session) {
        Map<Long, Integer> cart = (Map<Long, Integer>) session.getAttribute("cart");
        if (cart != null && cart.containsKey(productId)) {
            if (quantity <= 0) {
                cart.remove(productId);
            } else {
                cart.put(productId, quantity);
            }
        }
        return ResponseEntity.ok(Map.of("success", true));
    }

    @PostMapping("/shop/cart/remove")
    @ResponseBody
    @SuppressWarnings("unchecked")
    public ResponseEntity<?> removeFromCart(@RequestParam Long productId, HttpSession session) {
        Map<Long, Integer> cart = (Map<Long, Integer>) session.getAttribute("cart");
        if (cart != null) {
            cart.remove(productId);
        }
        return ResponseEntity.ok(Map.of("success", true));
    }

    @GetMapping("/shop/cart/items")
    @ResponseBody
    @SuppressWarnings("unchecked")
    public ResponseEntity<?> getCartItems(HttpSession session) {
        Map<Long, Integer> cart = (Map<Long, Integer>) session.getAttribute("cart");
        List<Map<String, Object>> items = new ArrayList<>();
        BigDecimal total = BigDecimal.ZERO;
        int totalItems = 0;
        
        if (cart != null) {
            for (Map.Entry<Long, Integer> entry : cart.entrySet()) {
                Product product = productRepository.findById(entry.getKey()).orElse(null);
                if (product != null) {
                    int qty = entry.getValue();
                    totalItems += qty;
                    BigDecimal subtotal = product.getPrice().multiply(BigDecimal.valueOf(qty));
                    total = total.add(subtotal);
                    items.add(Map.of(
                        "id", product.getId(),
                        "name", product.getName(),
                        "price", product.getPrice(),
                        "image", product.getImage() != null ? product.getImage() : "",
                        "quantity", qty,
                        "subtotal", subtotal,
                        "stock", product.getStock()
                    ));
                }
            }
        }
        return ResponseEntity.ok(Map.of("items", items, "total", total, "totalItems", totalItems));
    }

    @PostMapping("/shop/cart/checkout")
    @ResponseBody
    @Transactional
    @SuppressWarnings("unchecked")
    public ResponseEntity<?> checkout(HttpSession session, Principal principal) {
        if (principal == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "Vui lòng đăng nhập trước khi thanh toán."));
        }
        Map<Long, Integer> cart = (Map<Long, Integer>) session.getAttribute("cart");
        if (cart == null || cart.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("error", "Giỏ hàng của bạn đang trống."));
        }
        
        String username = principal.getName();
        UserAccount account = userAccountRepository.findByUsername(username).orElse(null);
        if (account == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "Không tìm thấy tài khoản người dùng."));
        }
        
        Customer customer = customerRepository.findByEmail(account.getEmail()).orElse(null);
        if (customer == null) {
            customer = new Customer();
            customer.setName(account.getFullName());
            customer.setEmail(account.getEmail());
            customer.setActive(true);
            customer = customerRepository.save(customer);
        }
        
        ShopOrder order = new ShopOrder();
        order.setCustomer(customer);
        order.setStatus(OrderStatus.NEW);
        order.setCreatedAt(LocalDateTime.now());
        
        for (Map.Entry<Long, Integer> entry : cart.entrySet()) {
            Product product = productRepository.findById(entry.getKey()).orElse(null);
            if (product != null) {
                int qtyNeeded = entry.getValue();
                if (product.getStock() < qtyNeeded) {
                    return ResponseEntity.badRequest().body(Map.of("error", "Sản phẩm '" + product.getName() + "' chỉ còn " + product.getStock() + " sản phẩm trong kho."));
                }
                
                // Deduct stock
                product.setStock(product.getStock() - qtyNeeded);
                productRepository.save(product);
                
                OrderItem item = new OrderItem();
                item.setProduct(product);
                item.setQuantity(qtyNeeded);
                item.setUnitPrice(product.getPrice());
                order.addItem(item);
            }
        }
        
        shopOrderRepository.save(order);
        session.removeAttribute("cart");
        
        return ResponseEntity.ok(Map.of("success", true, "orderId", order.getId()));
    }
}
