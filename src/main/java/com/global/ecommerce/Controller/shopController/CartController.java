package com.global.ecommerce.Controller.shopController;

import com.global.ecommerce.Entity.shopEntity.Cart;
import com.global.ecommerce.Entity.shopEntity.CartItem;
import com.global.ecommerce.DTO.shopDto.CartItemRequestDto;
import com.global.ecommerce.security.JwtTokenUtils;
import com.global.ecommerce.service.authService.UserService;
import com.global.ecommerce.service.shopService.CartService;
import com.global.ecommerce.service.walletService.WalletService;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/v1/carts")
public class CartController {
    @Autowired
    private CartService cartService;
    @Autowired
    private WalletService walletService;
    @Autowired
    private UserService userService;
    @Autowired
    JwtTokenUtils jwtTokenUtils;


    @Transactional
    @PostMapping("")
    public ResponseEntity<Cart> addItemToCart( @Valid @RequestBody CartItemRequestDto cartItemRequest,@NonNull HttpServletRequest req) {
        //todo:check if product exist in cart not add it again:: Done in Service!
        Long userId= jwtTokenUtils.getUserIdFromToken(req);
        try {
            if (!userService.userExistsById(userId)) {
                return ResponseEntity.badRequest().body(null);
            }
            Cart updatedCart = cartService.addItemToCart(userId, cartItemRequest.getProductId(), cartItemRequest.getQuantity());
            return ResponseEntity.ok(updatedCart);
        }
        finally {
//            JwtContext.clear();
        }
    }

    @GetMapping("")
    public ResponseEntity<List<CartItem>> getItemsByUserId(@NonNull HttpServletRequest req ) {
        Long userId= jwtTokenUtils.getUserIdFromToken(req);
        try {
            // Check if user exists
            if (!userService.userExistsById(userId)) {
                return ResponseEntity.badRequest().body(null);
            }
            List<CartItem> items = cartService.getItemsByUserId(userId);
            return ResponseEntity.ok(items);
        }finally {
        }
    }

    @DeleteMapping("")
    public ResponseEntity<Cart> deleteAllItemsInCart(@NonNull HttpServletRequest req) {
        Long userId= jwtTokenUtils.getUserIdFromToken(req);
        try {
            // Check if user exists
            if (!userService.userExistsById(userId)) {
                return ResponseEntity.badRequest().body(null);
            }
            Cart updatedCart = cartService.deleteAllItemsInCart(userId);
            return ResponseEntity.ok(updatedCart);
        }finally {
        }

    }

    @DeleteMapping("/item/{productId}")
    public ResponseEntity<Cart> deleteItemFromCart( @PathVariable Long productId,@NonNull HttpServletRequest req) {
        Long userId= jwtTokenUtils.getUserIdFromToken(req);
        try {
            // Check if user exists
            if (!userService.userExistsById(userId)) {
                return ResponseEntity.badRequest().body(null);
            }
            Cart updatedCart = cartService.deleteItemFromCart(userId, productId);
            return ResponseEntity.ok(updatedCart);
        } finally {
        }
    }

    // Not need Product Id i Will delete It
    //revision this function
    @PutMapping("/item")
    public ResponseEntity<Cart> updateCartItem(@Valid @RequestBody CartItemRequestDto cartItem,@NonNull HttpServletRequest req) {
        Long userId= jwtTokenUtils.getUserIdFromToken(req);
        try {
            // Check if user exists
            if (!userService.userExistsById(userId)) {
                return ResponseEntity.badRequest().body(null);
            }
            Cart updatedCart = cartService.updateCartItem(userId, cartItem.getProductId(),cartItem.getQuantity());
            return ResponseEntity.ok(updatedCart);
        }finally {
        }

    }
}
