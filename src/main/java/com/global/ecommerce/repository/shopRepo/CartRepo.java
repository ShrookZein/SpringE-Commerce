package com.global.ecommerce.repository.shopRepo;

import com.global.ecommerce.Entity.shopEntity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CartRepo extends JpaRepository<Cart,Long> {
    Optional<Cart>findByUserId(Long userId);
}
