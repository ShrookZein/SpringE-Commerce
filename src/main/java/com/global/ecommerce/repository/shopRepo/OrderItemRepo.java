package com.global.ecommerce.repository.shopRepo;

import com.global.ecommerce.Entity.shopEntity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderItemRepo extends JpaRepository<OrderItem,Long> {
}
