package com.global.ecommerce.service.inventoryService;

import com.global.ecommerce.Entity.inventoryEntity.ProductInventory;
import com.global.ecommerce.repository.inventoryRepo.ProductInventoryRepo;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Log4j2 // Add Log4j2 annotation
public class ProductInventoryService {
    @Autowired
    private ProductInventoryRepo productInventoryRepo;

    public ProductInventory createProductInventory(ProductInventory product) {
        log.info("Creating product inventory for productCode: {}", product.getProductCode());
        if (productInventoryRepo.findByProductCode(product.getProductCode()).isPresent()) {
            log.warn("Product already exists with productCode: {}", product.getProductCode());
            throw new RuntimeException("Product already exists");
        }
        ProductInventory savedProduct = productInventoryRepo.save(product);
        log.debug("Product inventory created successfully: {}", savedProduct);
        return savedProduct;
    }

    public void deleteProductInventory(Long productCode) {
        log.info("Deleting product inventory for productCode: {}", productCode);
        ProductInventory product = productInventoryRepo.findByProductCode(productCode)
                .orElseThrow(() -> {
                    log.error("Product not found for productCode: {}", productCode);
                    return new RuntimeException("Product not found: " + productCode);
                });
        productInventoryRepo.deleteById(product.getId());
        log.debug("Product inventory deleted for productCode: {}", productCode);
    }

    @Transactional
    public boolean isQuantityOfProductAvailable(Long productCode, Long quantity) {
        log.info("Checking if quantity is available for productCode: {}, quantity: {}", productCode, quantity);
        Optional<ProductInventory> product = productInventoryRepo.findByProductCode(productCode);
        boolean isAvailable = product.map(inventory -> inventory.getQuantity() - inventory.getReservedQuantity() >= quantity).orElse(false);
        log.debug("Quantity availability for productCode: {}, available: {}", productCode, isAvailable);
        return isAvailable;
    }

    public void reserveProduct(Long productCode, Long quantity) {
        log.info("Reserving product for productCode: {}, quantity: {}", productCode, quantity);
        ProductInventory product = productInventoryRepo.findByProductCode(productCode)
                .orElseThrow(() -> {
                    log.error("Product not found for productCode: {}", productCode);
                    return new RuntimeException("Product not found");
                });

        if (product.getQuantity() - product.getReservedQuantity() >= quantity) {
            product.setReservedQuantity(product.getReservedQuantity() + quantity);
            productInventoryRepo.save(product);
            log.debug("Product reserved successfully for productCode: {}, new reservedQuantity: {}", productCode, product.getReservedQuantity());
        } else {
            log.warn("Insufficient stock to reserve for productCode: {}", productCode);
            throw new RuntimeException("Insufficient stock to reserve");
        }
    }

    public void updateStock(Long productCode, Long quantity) {
        log.info("Updating stock for productCode: {}, quantity: {}", productCode, quantity);
        ProductInventory product = productInventoryRepo.findByProductCode(productCode)
                .orElseThrow(() -> {
                    log.error("Product not found for productCode: {}", productCode);
                    return new RuntimeException("Product not found");
                });

        product.setQuantity(product.getQuantity() - quantity);
        product.setReservedQuantity(product.getReservedQuantity() - quantity);
        productInventoryRepo.save(product);
        log.debug("Stock updated for productCode: {}, new quantity: {}, new reservedQuantity: {}", productCode, product.getQuantity(), product.getReservedQuantity());

        if (product.getQuantity() < product.getReorderThreshold()) {
            log.warn("Reorder needed for productCode: {}", productCode);
        }
    }

    public void handleReturn(Long productCode, Long quantity) {
        log.info("Handling return for productCode: {}, quantity: {}", productCode, quantity);
        ProductInventory product = productInventoryRepo.findByProductCode(productCode)
                .orElseThrow(() -> {
                    log.error("Product not found for productCode: {}", productCode);
                    return new RuntimeException("Product not found");
                });

        product.setQuantity(product.getQuantity() + quantity);
        Long reservedQuantity = product.getReservedQuantity() - quantity;
        if (reservedQuantity < 0) {
            log.error("Reserved quantity less than quantity to return for productCode: {}", productCode);
            throw new RuntimeException("Reserved quantity less than quantity you want to return");
        }
        product.setReservedQuantity(reservedQuantity);
        productInventoryRepo.save(product);
        log.debug("Return handled successfully for productCode: {}, new quantity: {}, new reservedQuantity: {}", productCode, product.getQuantity(), product.getReservedQuantity());
    }

    public Long getProductQuantity(Long productCode) {
        log.info("Getting product quantity for productCode: {}", productCode);
        ProductInventory product = productInventoryRepo.findByProductCode(productCode)
                .orElseThrow(() -> {
                    log.error("Product not found for productCode: {}", productCode);
                    return new RuntimeException("Product not found");
                });
        log.debug("Product quantity for productCode: {}, quantity: {}", productCode, product.getQuantity());
        return product.getQuantity();
    }

    public Long updateProductQuantity(Long productCode, Long quantity) {
        log.info("Updating product quantity for productCode: {}, quantity: {}", productCode, quantity);
        ProductInventory product = productInventoryRepo.findByProductCode(productCode)
                .orElseThrow(() -> {
                    log.error("Product not found for productCode: {}", productCode);
                    return new RuntimeException("Product not found");
                });

        product.setQuantity(product.getQuantity() + quantity);
        productInventoryRepo.save(product);
        log.debug("Product quantity updated for productCode: {}, new quantity: {}", productCode, product.getQuantity());
        return product.getQuantity();
    }
}








//package com.global.ecommerce.service.inventoryService;
//
//import com.global.ecommerce.Entity.inventoryEntity.ProductInventory;
//import com.global.ecommerce.repository.inventoryRepo.ProductInventoryRepo;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.util.Optional;
//
//@Service
//public class ProductInventoryService {
//    @Autowired
//    private ProductInventoryRepo productInventoryRepo;
//    public ProductInventory createProductInventory(ProductInventory product){
//        if(productInventoryRepo.findByProductCode(product.getProductCode()).isPresent()){
//            throw new RuntimeException("product already exists");
//        }
//        return productInventoryRepo.save(product);
//    }
//    public void deleteProductInventory(Long productCode){
//        ProductInventory product= productInventoryRepo.findByProductCode(productCode).orElseThrow(()->new RuntimeException("product not found: " + productCode));
//        productInventoryRepo.deleteById(product.getId());
//    }
//    @Transactional
//    public boolean isQuantityOfProductAvailable(Long productCode, Long quantity) {
//        Optional<ProductInventory> product = productInventoryRepo.findByProductCode(productCode);
////        System.out.println("Checking inventory for productCode: " + product.get().getProductCode());
////        System.out.println("Checking inventory for productCode: " + product.get().getQuantity());
////        Long x=product.get().getQuantity();
////        Long y=product.get().getReservedQuantity();
////        System.out.println("Checking inventory for productCode: "+x+"     " +y);
////        if(x-y>=quantity){
////            return true;
////        }
//        return product.map(inventory -> inventory.getQuantity() - inventory.getReservedQuantity() >= quantity).orElse(false);
////        return false;
//    }
//
//    public void reserveProduct(Long productCode, Long quantity) {
//        ProductInventory product = productInventoryRepo.findByProductCode(productCode)
//                .orElseThrow(() -> new RuntimeException("Product not found"));
//
//        if(product.getQuantity()-product.getReservedQuantity()>=quantity){
//            product.setReservedQuantity(product.getReservedQuantity()+quantity);
//            productInventoryRepo.save(product);
//        }else {
//            throw new RuntimeException("Insufficient stock to reserve");
//        }
//    }
//    public void updateStock(Long productCode, Long quantity) {
//        ProductInventory product = productInventoryRepo.findByProductCode(productCode)
//                .orElseThrow(() -> new RuntimeException("Product not found"));
//
//        product.setQuantity(product.getQuantity() - quantity);
//        product.setReservedQuantity(product.getReservedQuantity() - quantity);
//        productInventoryRepo.save(product);
//
//        if (product.getQuantity() < product.getReorderThreshold()) {
//            System.out.println("Reorder needed for product: " + productCode);
//        }
//    }
//    public void handleReturn(Long productCode, Long quantity) {
//        ProductInventory product = productInventoryRepo.findByProductCode(productCode)
//                .orElseThrow(() -> new RuntimeException("Product not found"));
//
//        product.setQuantity(product.getQuantity() + quantity);
//        Long reservedQuantity =product.getReservedQuantity() - quantity;
//        if (reservedQuantity < 0) {
//            throw new RuntimeException("reserved Quantity less than quantity you want to return");
//        }
//        product.setReservedQuantity(reservedQuantity);
//        productInventoryRepo.save(product);
//    }
//    public Long getProductQuantity(Long productCode){
//        ProductInventory product= productInventoryRepo.findByProductCode(productCode)
//                .orElseThrow(() -> new RuntimeException("Product not found"));
//        return product.getQuantity();
//    }
//    public Long updateProductQuantity(Long productCode,Long quantity){
//        ProductInventory product= productInventoryRepo.findByProductCode(productCode)
//                .orElseThrow(() -> new RuntimeException("Product not found"));
//        product.setQuantity(product.getQuantity()+quantity);
//        productInventoryRepo.save(product);
//
//        return product.getQuantity();
//    }
//
//}





