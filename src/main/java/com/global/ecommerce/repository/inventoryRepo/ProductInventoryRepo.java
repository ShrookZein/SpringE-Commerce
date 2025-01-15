package com.global.ecommerce.repository.inventoryRepo;

import com.global.ecommerce.Entity.inventoryEntity.ProductInventory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface ProductInventoryRepo extends JpaRepository<ProductInventory,Long> {
    Optional<ProductInventory>findByProductCode(Long productCode);
    void deleteByProductCode(Long productCode);
}
