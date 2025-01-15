package com.global.ecommerce.repository.shopRepo;

import com.global.ecommerce.Entity.shopEntity.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CartItemRepo extends JpaRepository<CartItem,Long> {
    List<CartItem>findByCartId(Long cartId);
}
