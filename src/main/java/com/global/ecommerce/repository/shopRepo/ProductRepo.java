package com.global.ecommerce.repository.shopRepo;

import com.global.ecommerce.Entity.shopEntity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepo extends JpaRepository<Product,Long> {
}
