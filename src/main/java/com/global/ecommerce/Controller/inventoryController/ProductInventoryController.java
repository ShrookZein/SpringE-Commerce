package com.global.ecommerce.Controller.inventoryController;

import com.global.ecommerce.DTO.inventoryDto.ProInvReqDto;
import com.global.ecommerce.Entity.inventoryEntity.ProductInventory;
import com.global.ecommerce.service.inventoryService.ProductInventoryService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

@Validated
@RestController
@RequestMapping("/api/v1/inventory")
public class ProductInventoryController {
    @Autowired
    ProductInventoryService productService;
    @Autowired
    private ModelMapper modelMapper;
    @PostMapping()
    public ResponseEntity<?> createProductInventory(@Valid @RequestBody ProInvReqDto product) {
        ProductInventory productInventory=modelMapper.map(product,ProductInventory.class);
        return ResponseEntity.ok(productService.createProductInventory(productInventory));
    }

    @DeleteMapping("/{productCode}")
    public ResponseEntity<?> deleteProductInventory(@PathVariable Long productCode) {
        productService.deleteProductInventory(productCode);
        return ResponseEntity.ok("ProductInventory deleted successfully");
    }
    @GetMapping("/{productCode}/availability")
    public ResponseEntity<?> isProductAvailable(@PathVariable Long productCode,@Min(value= 1,message = "quantity must be greater than or equal 1") @RequestParam Long quantity) {
        return ResponseEntity.ok(productService.isQuantityOfProductAvailable(productCode, quantity));
    }

    @PostMapping("/{productCode}/reserve")
    public ResponseEntity<?> reserveProduct(@PathVariable Long productCode,@Min(value= 1,message = "quantity must be greater than or equal 1") @RequestParam Long quantity) {
        productService.reserveProduct(productCode, quantity);
        return ResponseEntity.ok("Product reserve successfully");
    }

    @PutMapping("/{productCode}/update")
    public ResponseEntity<?> updateStock(@PathVariable Long productCode, @RequestParam Long quantity) {
        productService.updateStock(productCode, quantity);
        return ResponseEntity.ok("Stock updated successfully");
    }

    @PostMapping("/{productCode}/return")
    public ResponseEntity<?> handleReturn(@PathVariable Long productCode, @RequestParam Long quantity) {
        productService.handleReturn(productCode, quantity);
        return ResponseEntity.ok("Product returned successfully");
    }
    @GetMapping("/{productCode}/quantity")
    public ResponseEntity<?> getProductQuantity(@PathVariable Long productCode) {
        return ResponseEntity.ok(productService.getProductQuantity(productCode));
    }
    @PutMapping("/{productCode}")
    public ResponseEntity<?> updateProductQuantity(@PathVariable Long productCode, @RequestParam Long quantity) {
        return ResponseEntity.ok(productService.updateProductQuantity(productCode, quantity));
    }
}
