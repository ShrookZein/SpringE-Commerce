package com.global.ecommerce.controller.shopControllerTest;

import com.global.ecommerce.DTO.shopDto.ProductRequestDto;
import com.global.ecommerce.DTO.shopDto.ProductResponseDto;
import com.global.ecommerce.Entity.shopEntity.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;

import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class OrderControllerTest {
    @Autowired
    private TestRestTemplate testRestTemplate;

    //Enter Valid Token
    String token="eyJhbGciOiJIUzUxMiJ9.eyJqdGkiOiIyN2YyZjMxMS1lOThhLTQwODYtODFmOC05MDc3MmE3N2UwZjkiLCJzdWIiOiJTaHJvb2tra2tAZ21haWwuY29tIiwiaWF0IjoxNzM2ODUxMzU1LCJpc3MiOiJhcHAtU2VydmljZSIsImV4cCI6MTczNzAzMTM1NSwiY3JlYXRlZCI6MTczNjg1MTM1NTM0MX0.5nT4xiFNRECvcoWpsEzQFG26G69klLVNAWUAlv2y1umLRhPx5sOzevxoRxLbcJr_Q-EafK_ZGNYMPWbJYt-qww";
    HttpHeaders headers = new HttpHeaders();

    @Test
    void createOrder(){
        headers.setBearerAuth(token);
        HttpEntity<Void> requestEntity=new HttpEntity<>(headers);
        ResponseEntity<Order> response=testRestTemplate.exchange("/api/v1/orders", HttpMethod.POST,requestEntity,Order.class);
        assertEquals(HttpStatus.OK,response.getStatusCode());
    }
    @Test
    void getOrdersByUserId(){
        headers.setBearerAuth(token);
        HttpEntity<Void> requestEntity=new HttpEntity<>(headers);
        ResponseEntity<List<Order>> response=testRestTemplate.exchange("/api/v1/orders/", HttpMethod.GET, requestEntity, new ParameterizedTypeReference<>(){});
        assertEquals(HttpStatus.OK,response.getStatusCode());
    }
    @Test
    void getOrderById(){
        headers.setBearerAuth(token);
        HttpEntity<Void> requestEntity=new HttpEntity<>(headers);
        ResponseEntity<Order> response=testRestTemplate.exchange("/api/v1/orders/specific/"+4L, HttpMethod.GET, requestEntity, Order.class);
        assertEquals(HttpStatus.OK,response.getStatusCode());
    }
}
