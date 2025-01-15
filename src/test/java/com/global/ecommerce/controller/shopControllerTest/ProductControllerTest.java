package com.global.ecommerce.controller.shopControllerTest;

import com.global.ecommerce.DTO.shopDto.ProductRequestDto;
import com.global.ecommerce.DTO.shopDto.ProductResponseDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;

import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ProductControllerTest {
    @Autowired
    private TestRestTemplate testRestTemplate;

    //Enter Valid Token
    String token="eyJhbGciOiJIUzUxMiJ9.eyJqdGkiOiIyN2YyZjMxMS1lOThhLTQwODYtODFmOC05MDc3MmE3N2UwZjkiLCJzdWIiOiJTaHJvb2tra2tAZ21haWwuY29tIiwiaWF0IjoxNzM2ODUxMzU1LCJpc3MiOiJhcHAtU2VydmljZSIsImV4cCI6MTczNzAzMTM1NSwiY3JlYXRlZCI6MTczNjg1MTM1NTM0MX0.5nT4xiFNRECvcoWpsEzQFG26G69klLVNAWUAlv2y1umLRhPx5sOzevxoRxLbcJr_Q-EafK_ZGNYMPWbJYt-qww";
    HttpHeaders headers = new HttpHeaders();

    @Test
    void createProduct_WithValidBearerToken() {
        headers.setBearerAuth(token);

        ProductRequestDto productRequestDto = new ProductRequestDto();
        productRequestDto.setName("Test Product");
        productRequestDto.setPrice(100.0);
        productRequestDto.setDescription("Test Description");
        productRequestDto.setQuantity(200L);
        productRequestDto.setReorderThreshold(10L);

        HttpEntity<ProductRequestDto> requestEntity = new HttpEntity<>(productRequestDto, headers);

        ResponseEntity<ProductResponseDto> response = testRestTemplate.exchange(
                "/api/v1/products",
                HttpMethod.POST,
                requestEntity,
                ProductResponseDto.class
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        ProductResponseDto responseBody = response.getBody();
        assertNotNull(responseBody);
        assertEquals("Test Product", responseBody.getName());
        assertEquals(100.0, responseBody.getPrice());
        assertEquals("Test Description", responseBody.getDescription());
    }
    @Test
    void getAllProducts_WithValidBearerToken() {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);

        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);
        ResponseEntity<List<ProductResponseDto>> response = testRestTemplate.exchange(
                "/api/v1/products",
                HttpMethod.GET,
                requestEntity,
                new ParameterizedTypeReference<>() {}
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }
    @Test
    void getProductById(){
        headers.setBearerAuth(token);
        HttpEntity<Void>requestEntity=new HttpEntity<>(headers);
        ResponseEntity<ProductResponseDto>response=testRestTemplate.exchange("/api/v1/products/"+1041L,HttpMethod.GET,requestEntity,ProductResponseDto.class);
        assertEquals(HttpStatus.OK,response.getStatusCode());
        assertEquals(15, Objects.requireNonNull(response.getBody()).getPrice());
        assertNotNull(response.getBody().getName());
    }
    @Test
    void updateProduct(){
        headers.setBearerAuth(token);
        ProductRequestDto productRequestDto=new ProductRequestDto();
        productRequestDto.setName("New Product2");
        productRequestDto.setDescription("Hiiiiiiiii");
        productRequestDto.setPrice(22.0);
        productRequestDto.setQuantity(500L);
        productRequestDto.setReorderThreshold(2L);
        HttpEntity<ProductRequestDto>requestEntity=new HttpEntity<>(productRequestDto,headers);
        ResponseEntity<ProductResponseDto>response=testRestTemplate.exchange("/api/v1/products/"+1041L,HttpMethod.PUT,requestEntity,ProductResponseDto.class);
        assertEquals(HttpStatus.OK,response.getStatusCode());
        assertEquals(22.0, Objects.requireNonNull(response.getBody()).getPrice());
    }
    @Test
    void deleteProduct(){
        headers.setBearerAuth(token);
        HttpEntity<Void>requestEntity=new HttpEntity<>(headers);
        ResponseEntity<Void>response=testRestTemplate.exchange("/api/v1/products/"+1035L,HttpMethod.DELETE,requestEntity,Void.class);
        assertEquals(HttpStatus.NO_CONTENT,response.getStatusCode());
    }
    @Test
    void returnProduct(){
        headers.setBearerAuth(token);
        HttpEntity<Void>requestEntity=new HttpEntity<>(headers);
        ResponseEntity<Void>response=testRestTemplate.exchange("/api/v1/products/"+1028L+"/return?quantity="+1,HttpMethod.POST,requestEntity,Void.class);
        assertEquals(HttpStatus.OK,response.getStatusCode());
    }
}
