package com.global.ecommerce.Controller.shopController;

import com.global.ecommerce.Entity.shopEntity.Order;
import com.global.ecommerce.security.JwtTokenUtils;
import com.global.ecommerce.service.shopService.OrderService;
import com.global.ecommerce.service.walletService.WalletService;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/api/v1/orders")
public class OrderController {
    @Autowired
    private OrderService orderService;
    @Autowired
    private WalletService walletService;
    @Autowired
    JwtTokenUtils jwtTokenUtils;
    @Transactional
    @PostMapping("")
    public ResponseEntity<?> createOrder(@NonNull HttpServletRequest req){
        Long userId= jwtTokenUtils.getUserIdFromToken(req);
        // Validate token and user
        Order order = orderService.createOrder(req,userId);
        return ResponseEntity.ok(order);
    }
    @GetMapping("")
    public ResponseEntity<List<Order>> getOrdersByUserId(@NonNull HttpServletRequest req) {
        Long userId= jwtTokenUtils.getUserIdFromToken(req);
        // Validate token and user
        List<Order> orders = orderService.getOrdersByUserId(userId);
        return ResponseEntity.ok(orders);
    }
    @GetMapping("/specific/{orderId}")
    public ResponseEntity<Order> getOrderById(@PathVariable Long orderId) {
        // Validate token and user
        return orderService.getOrderById(orderId)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(404).body(null));
    }

}
