package com.global.ecommerce.Controller.shopController;

import com.global.ecommerce.Entity.shopEntity.Product;
import com.global.ecommerce.DTO.shopDto.ProductRequestDto;
import com.global.ecommerce.DTO.shopDto.ProductResponseDto;
import com.global.ecommerce.service.shopService.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.List;
@Validated
@RestController
@RequestMapping("/api/v1/products")
public class ProductController {
    @Autowired
    private ProductService productService;
    @Transactional
    @PostMapping
    public ResponseEntity<?> createProduct(@Valid @RequestBody ProductRequestDto productRequestDto) {
        return ResponseEntity.ok(productService.createProduct(productRequestDto));
    }

    @GetMapping
    public ResponseEntity<List<ProductResponseDto>> getAllProducts() {
        return ResponseEntity.ok(productService.getAllProducts());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponseDto> getProductById(@PathVariable Long id) {
        return ResponseEntity.ok(productService.getProductById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateProduct(@PathVariable Long id, @Valid @RequestBody ProductRequestDto updatedProduct) {
        return ResponseEntity.ok(productService.updateProduct(id, updatedProduct));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }
    @PostMapping("/{id}/return")
    public ResponseEntity<Void> returnProduct(@PathVariable Long id, @Min(value= 1,message = "quantity must be greater than or equal 1")@RequestParam Long quantity) {
        productService.returnProduct(id, quantity);
        return ResponseEntity.ok().build();
    }
}
