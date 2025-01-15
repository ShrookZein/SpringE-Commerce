package com.global.ecommerce.service.shopService;

import com.global.ecommerce.Entity.inventoryEntity.ProductInventory;
import com.global.ecommerce.Entity.shopEntity.Product;
import com.global.ecommerce.DTO.shopDto.ProductRequestDto;
import com.global.ecommerce.DTO.shopDto.ProductResponseDto;
import com.global.ecommerce.repository.shopRepo.ProductRepo;
import com.global.ecommerce.service.inventoryService.ProductInventoryService;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
@NoArgsConstructor
@Log4j2
public class ProductService {
    @Autowired
    private ProductRepo productRepo;
    @Autowired
    private ProductInventoryService productInventoryService;
    @Autowired
    private ModelMapper modelMapper;

    @Transactional
    public ProductResponseDto createProduct(ProductRequestDto productRequest) {
        log.info("Creating a new product with request: {}", productRequest);
        Product product = Product.builder()
                .name(productRequest.getName())
                .description(productRequest.getDescription())
                .price(productRequest.getPrice())
                .build();
        Product saveProduct = productRepo.save(product);
        log.info("Product saved: {}", saveProduct);

        ProductInventory productInventory;
        try {
            productInventory = ProductInventory.builder()
                    .productCode(saveProduct.getId())
                    .quantity(productRequest.getQuantity())
                    .reorderThreshold(productRequest.getReorderThreshold()).build();
            productInventoryService.createProductInventory(productInventory);
            log.info("Product inventory created: {}", productInventory);
        } catch (Exception e) {
            log.error("Failed to create inventory for the product: {}", saveProduct, e);
            productRepo.delete(saveProduct);
            throw new RuntimeException("Failed to create inventory for the product", e);
        }
        ProductResponseDto productResponseDto = modelMapper.map(saveProduct, ProductResponseDto.class);
        productResponseDto.setQuantity(productRequest.getQuantity());
        log.info("Product creation completed with response: {}", productResponseDto);
        return productResponseDto;
    }

    public List<ProductResponseDto> getAllProducts() {
        log.info("Fetching all products.");
        List<Product> products = productRepo.findAll();
        List<ProductResponseDto> productResponsDtos = new ArrayList<>();
        for (Product product : products) {
            Long quantity = productInventoryService.getProductQuantity(product.getId());
            ProductResponseDto response = ProductResponseDto.builder()
                    .id(product.getId())
                    .name(product.getName())
                    .description(product.getDescription())
                    .price(product.getPrice())
                    .quantity(quantity).build();
            productResponsDtos.add(response);
        }
        log.info("Fetched {} products.", productResponsDtos.size());
        return productResponsDtos;
    }

    public ProductResponseDto getProductById(Long productId) {
        log.info("Fetching product by ID: {}", productId);
        Product product = productRepo.findById(productId)
                .orElseThrow(() -> {
                    log.error("Product not found with ID: {}", productId);
                    return new RuntimeException("Product not found");
                });
        Long quantity = productInventoryService.getProductQuantity(productId);
        ProductResponseDto response = ProductResponseDto.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .quantity(quantity).build();
        log.info("Fetched product: {}", response);
        return response;
    }

    @Transactional
    public ProductResponseDto updateProduct(Long productId, ProductRequestDto productRequest) {
        log.info("Updating product with ID: {} using request: {}", productId, productRequest);
        Product product = productRepo.findById(productId)
                .orElseThrow(() -> {
                    log.error("Product not found with ID: {}", productId);
                    return new RuntimeException("Product with ID " + productId + " not found");
                });
        product.setName(productRequest.getName());
        product.setDescription(productRequest.getDescription());
        product.setPrice(productRequest.getPrice());
        product = productRepo.save(product);
        log.info("Product updated: {}", product);

        Long quantity = productInventoryService.updateProductQuantity(productId, productRequest.getQuantity());
        ProductResponseDto productResponseDto = modelMapper.map(product, ProductResponseDto.class);
        productResponseDto.setQuantity(quantity);
        log.info("Product update completed with response: {}", productResponseDto);
        return productResponseDto;
    }

    @Transactional
    public void deleteProduct(Long productId) {
        log.info("Deleting product with ID: {}", productId);
        Product product = productRepo.findById(productId)
                .orElseThrow(() -> {
                    log.error("Product not found with ID: {}", productId);
                    return new RuntimeException("Product with ID " + productId + " not found");
                });
        try {
            productRepo.deleteById(productId);
            productInventoryService.deleteProductInventory(productId);
            log.info("Product and its inventory deleted successfully.");
        } catch (Exception e) {
            log.error("Failed to delete product or its inventory with ID: {}", productId, e);
            throw new RuntimeException("Failed to delete inventory for the product", e);
        }
    }

    public void returnProduct(Long productId, Long quantity) {
        log.info("Returning product with ID: {} and quantity: {}", productId, quantity);
        Product product = productRepo.findById(productId)
                .orElseThrow(() -> {
                    log.error("Product not found with ID: {}", productId);
                    return new RuntimeException("Product not found");
                });
        productInventoryService.handleReturn(productId, quantity);
        log.info("Product return handled successfully for ID: {} and quantity: {}", productId, quantity);
    }
}










//package com.global.ecommerce.service.shopService;
//
//import com.global.ecommerce.Entity.inventoryEntity.ProductInventory;
//import com.global.ecommerce.Entity.shopEntity.Product;
//import com.global.ecommerce.DTO.shopDto.ProductRequestDto;
//import com.global.ecommerce.DTO.shopDto.ProductResponseDto;
//import com.global.ecommerce.repository.shopRepo.ProductRepo;
//import com.global.ecommerce.service.inventoryService.ProductInventoryService;
//import lombok.AllArgsConstructor;
//import lombok.NoArgsConstructor;
//import org.modelmapper.ModelMapper;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.util.ArrayList;
//import java.util.List;
//
//@Service
//@AllArgsConstructor
//@NoArgsConstructor
//public class ProductService {
//    @Autowired
//    private ProductRepo productRepo;
//    @Autowired
//    private ProductInventoryService productInventoryService;
//    @Autowired
//    private ModelMapper modelMapper;
//    @Transactional
//    public ProductResponseDto createProduct(ProductRequestDto productRequest){
//
//        Product product= Product.builder()
//                .name(productRequest.getName())
//                .description(productRequest.getDescription())
//                .price(productRequest.getPrice())
//                .build();
//        Product saveProduct=productRepo.save(product);
//        ProductInventory productInventory=null;
//        try{
//            productInventory=ProductInventory.builder()
//                    .productCode(saveProduct.getId())
//                    .quantity(productRequest.getQuantity())
//                    .reorderThreshold(productRequest.getReorderThreshold()).build();
//            productInventoryService.createProductInventory(productInventory);
//        }catch (Exception e){
//            productRepo.delete(saveProduct);
//            throw new RuntimeException("Failed to create inventory for the product", e);
//        }
//        ProductResponseDto productResponseDto=modelMapper.map(saveProduct,ProductResponseDto.class);
//        productResponseDto.setQuantity(productRequest.getQuantity());
//        return productResponseDto;
//    }
//    public List<ProductResponseDto>getAllProducts(){
//        List<Product>products=productRepo.findAll();
//        List<ProductResponseDto> productResponsDtos =new ArrayList<>();
//        for(Product product:products){
//            Long quantity=productInventoryService.getProductQuantity(product.getId());
//            ProductResponseDto response= ProductResponseDto.builder()
//                    .id(product.getId())
//                    .name(product.getName())
//                    .description(product.getDescription())
//                    .price(product.getPrice())
//                    .quantity(quantity).build();
//            productResponsDtos.add(response);
//        }
//        return productResponsDtos;
//    }
//    public ProductResponseDto getProductById(Long productId){
//        Product product=productRepo.findById(productId).orElseThrow(()->new RuntimeException("Product not found"));
//        Long quantity=productInventoryService.getProductQuantity(productId);
//        return ProductResponseDto.builder()
//                .id(product.getId())
//                .name(product.getName())
//                .description(product.getDescription())
//                .price(product.getPrice())
//                .quantity(quantity).build();
//    }
//    @Transactional
//    public ProductResponseDto updateProduct(Long productId, ProductRequestDto productRequest){
//        Product product=productRepo.findById(productId)
//                .orElseThrow(()->new RuntimeException("Product with ID " + productId + " not found"));
//        product.setName(productRequest.getName());
//        product.setDescription(productRequest.getDescription());
//        product.setPrice(productRequest.getPrice());
//        product=productRepo.save(product);
//
//        // Update the quantity in the inventory service
//        Long quantity=productInventoryService.updateProductQuantity(productId,productRequest.getQuantity());
//        ProductResponseDto productResponseDto=modelMapper.map(product,ProductResponseDto.class);
//        productResponseDto.setQuantity(quantity);
//        return productResponseDto;
//    }
//
//    @Transactional
//    public void deleteProduct(Long productId) {
//        Product product =  productRepo.findById(productId)
//                .orElseThrow(() -> new RuntimeException("Product with ID " + productId + " not found"));
//
//        try {
//            productRepo.deleteById(productId);
//            productInventoryService.deleteProductInventory(productId);
//        } catch (Exception e) {
//            throw new RuntimeException("Failed to delete inventory for the product", e);
//        }
//    }
//    public void returnProduct(Long productId, Long quantity) {
//        Product product = productRepo.findById(productId)
//                .orElseThrow(() -> new RuntimeException("Product not found"));
//        productInventoryService.handleReturn(productId, quantity);
//    }
//}







