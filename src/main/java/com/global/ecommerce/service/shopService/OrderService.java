package com.global.ecommerce.service.shopService;

import com.global.ecommerce.Controller.walletController.WalletController;
import com.global.ecommerce.Entity.shopEntity.*;
import com.global.ecommerce.repository.shopRepo.OrderItemRepo;
import com.global.ecommerce.repository.shopRepo.OrderRepo;
import com.global.ecommerce.service.inventoryService.ProductInventoryService;
import com.global.ecommerce.service.walletService.WalletService;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
@NoArgsConstructor
@Log4j2
public class OrderService {

    @Autowired
    private OrderRepo orderRepo;

    @Autowired
    private OrderItemRepo orderItemRepo;

    @Autowired
    private WalletService walletService;

    @Autowired
    WalletController walletController;

    @Autowired
    private CartService cartService;

    @Autowired
    private ProductInventoryService productInventoryService;

    @Transactional
    public Order createOrder(HttpServletRequest req,Long userId) {
        log.info("Creating order for userId: {}", userId);
        List<CartItem> cartItems = cartService.getItemsByUserId(userId);
        log.debug("Retrieved {} cart items for userId: {}", cartItems.size(), userId);

        List<OrderItem> orderItems = new ArrayList<>();
        BigDecimal totalAmount = BigDecimal.ZERO;

        for (CartItem cartItem : cartItems) {
            Product product = cartItem.getProduct();
            Long productCode = product.getId();
            Long quantity = cartItem.getQuantity();

            log.debug("Checking inventory for productCode: {}, quantity: {}", productCode, quantity);
            if (!productInventoryService.isQuantityOfProductAvailable(productCode, quantity)) {
                log.error("Insufficient quantity for productCode: {}", productCode);
                throw new RuntimeException("This quantity of Product " + productCode + " is not available.");
            }

            BigDecimal price = BigDecimal.valueOf(product.getPrice()).multiply(BigDecimal.valueOf(quantity));
            totalAmount = totalAmount.add(price);

            OrderItem orderItem = OrderItem.builder()
                    .product(product)
                    .quantity(quantity)
                    .price(price)
                    .build();

            orderItemRepo.save(orderItem);
            log.debug("Saved order item for productCode: {}, quantity: {}, price: {}", productCode, quantity, price);
            orderItems.add(orderItem);

            productInventoryService.reserveProduct(productCode, quantity);
            log.debug("Reserved productCode: {}, quantity: {}", productCode, quantity);
        }

        BigDecimal walletBalance = getWalletBalance(userId);
        log.debug("Wallet balance for userId: {} is {}", userId, walletBalance);

        if (walletBalance.compareTo(totalAmount) < 0) {
            log.error("Insufficient balance in wallet for userId: {}. Required: {}, Available: {}", userId, totalAmount, walletBalance);
            throw new RuntimeException("Insufficient balance in the wallet.");
        }

        withdrawFromWallet(req, totalAmount);
        log.info("Withdrawn {} from wallet for userId: {}", totalAmount, userId);

        Order order = Order.builder()
                .orderItems(orderItems)
                .status("PENDING")
                .totalAmount(totalAmount)
                .userId(userId)
                .createdAt(new Timestamp(System.currentTimeMillis()))
                .build();

        Order savedOrder = orderRepo.save(order);
        log.info("Order created with id: {} for userId: {}", savedOrder.getId(), userId);

        for (OrderItem orderItem : orderItems) {
            productInventoryService.updateStock(orderItem.getProduct().getId(), orderItem.getQuantity());
            log.debug("Updated stock for productCode: {}, quantity: {}", orderItem.getProduct().getId(), orderItem.getQuantity());

            orderItem.setOrder(order);
            orderItemRepo.save(orderItem);
            log.debug("Linked order item to order id: {}", savedOrder.getId());
        }

        cartService.deleteAllItemsInCart(userId);
        log.info("Cleared cart for userId: {}", userId);

        return savedOrder;
    }

    private BigDecimal getWalletBalance(Long userId) {
        log.debug("Fetching wallet balance for userId: {}", userId);
        return walletService.getWalletBalance(userId);
    }

    private void withdrawFromWallet(HttpServletRequest req, BigDecimal amount) {
//        log.debug("Attempting to withdraw {} from wallet for userId: {}", amount, userId);
        try {
            walletController.withdraw(req, amount);
//            log.info("Successfully withdrawn {} from wallet for userId: {}", amount, userId);
        } catch (Exception e) {
//            log.error("Error withdrawing from wallet for userId: {}: {}", userId, e.getMessage());
            throw new RuntimeException("Error withdrawing from wallet: " + e.getMessage());
        }
    }

    public List<Order> getOrdersByUserId(Long userId) {
        log.debug("Fetching orders for userId: {}", userId);
        return orderRepo.findByUserId(userId);
    }

    public Optional<Order> getOrderById(Long orderId) {
        log.debug("Fetching order by id: {}", orderId);
        return orderRepo.findById(orderId);
    }
}






//package com.global.ecommerce.service.shopService;
//
//import com.global.ecommerce.Controller.walletController.WalletController;
//import com.global.ecommerce.Entity.shopEntity.*;
//import com.global.ecommerce.repository.shopRepo.OrderItemRepo;
//import com.global.ecommerce.repository.shopRepo.OrderRepo;
//import com.global.ecommerce.service.inventoryService.ProductInventoryService;
//import com.global.ecommerce.service.walletService.WalletService;
//import lombok.AllArgsConstructor;
//import lombok.NoArgsConstructor;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.ResponseEntity;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.math.BigDecimal;
//import java.sql.Timestamp;
//import java.time.LocalDateTime;
//import java.util.ArrayList;
//import java.util.Date;
//import java.util.List;
//import java.util.Optional;
//
//@Service
//@AllArgsConstructor
//@NoArgsConstructor
//public class OrderService {
//    @Autowired
//    private OrderRepo orderRepo;
//    @Autowired
//    private OrderItemRepo orderItemRepo;
//    @Autowired
//    private WalletService walletService;
//    @Autowired
//    WalletController walletController;
//    @Autowired
//    private CartService cartService;
//    @Autowired
//    private ProductInventoryService productInventoryService;
//
//    @Transactional
//    public Order createOrder(Long userId){
////        Cart cart=cartService.getCartByUserId(userId).orElseThrow();
//        List<CartItem>cartItems=cartService.getItemsByUserId(userId);
//        List<OrderItem>orderItems=new ArrayList<>();
//        BigDecimal totalAmount=BigDecimal.ZERO;
//        System.out.println(totalAmount);
//
//        for(CartItem cartItem:cartItems){
//            Product product=cartItem.getProduct();
//            Long productCode=product.getId();
//            Long quantity=cartItem.getQuantity();
//            if(!productInventoryService.isQuantityOfProductAvailable(productCode,quantity)){
//                throw new RuntimeException("This quantity of Product "+productCode+" is not available.");
//            }
//            BigDecimal price= BigDecimal.valueOf(product.getPrice()).multiply(BigDecimal.valueOf(quantity));
//            totalAmount = totalAmount.add(price);
//            OrderItem orderItem=OrderItem.builder()
//                    .product(product)
//                    .quantity(quantity)
//                    .price(price)
//                    .build();
//            orderItemRepo.save(orderItem);
//            orderItems.add(orderItem);
//            productInventoryService.reserveProduct(productCode,quantity);
//        }
//        BigDecimal walletBalance = getWalletBalance(userId);
//        if(walletBalance.compareTo(totalAmount)<0){
//            throw new RuntimeException("Insufficient balance in the wallet.");
//        }
//        System.out.println("Hiiiiiiiiiiii------>"+totalAmount);
//        withdrawFromWallet(userId,totalAmount);
//        Order order=Order.builder()
//                .orderItems(orderItems)
//                .status("PENDING")
//                .totalAmount(totalAmount)
//                .userId(userId)
////                .createdAt(LocalDateTime.now())
//                .createdAt(new Timestamp(System.currentTimeMillis()))
//                .build();
////        order=orderRepo.save(order);
//        Order order1=orderRepo.save(order);
//        for(OrderItem orderItem:orderItems){
//            productInventoryService.updateStock(orderItem.getProduct().getId(),orderItem.getQuantity());
//            orderItem.setOrder(order);
//            orderItemRepo.save(orderItem);
//        }
//        cartService.deleteAllItemsInCart(userId);
//        return order1;
//    }
//    private BigDecimal getWalletBalance(Long userId) {
//        return walletService.getWalletBalance(userId);
//    }
//
//    private void withdrawFromWallet(Long userId, BigDecimal amount) {
//        try {
//            walletController.withdraw(userId, amount);
//        } catch (Exception e) {
//            throw new RuntimeException("Error withdrawing from wallet: " + e.getMessage());
//        }
//    }
//    public List<Order> getOrdersByUserId(Long userId) {
//        return orderRepo.findByUserId(userId);
//    }
//    public Optional<Order> getOrderById(Long orderId) {
//        return orderRepo.findById(orderId);
//    }
//}





