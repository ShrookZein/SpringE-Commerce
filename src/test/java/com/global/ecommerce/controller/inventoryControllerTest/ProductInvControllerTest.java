package com.global.ecommerce.controller.inventoryControllerTest;

import com.global.ecommerce.DTO.inventoryDto.ProInvReqDto;
import com.global.ecommerce.Entity.inventoryEntity.ProductInventory;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment= SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ProductInvControllerTest {
    @Autowired
    TestRestTemplate testRestTemplate;
    //Enter Valid Token
    String token="eyJhbGciOiJIUzUxMiJ9.eyJqdGkiOiIyN2YyZjMxMS1lOThhLTQwODYtODFmOC05MDc3MmE3N2UwZjkiLCJzdWIiOiJTaHJvb2tra2tAZ21haWwuY29tIiwiaWF0IjoxNzM2ODUxMzU1LCJpc3MiOiJhcHAtU2VydmljZSIsImV4cCI6MTczNzAzMTM1NSwiY3JlYXRlZCI6MTczNjg1MTM1NTM0MX0.5nT4xiFNRECvcoWpsEzQFG26G69klLVNAWUAlv2y1umLRhPx5sOzevxoRxLbcJr_Q-EafK_ZGNYMPWbJYt-qww";
    HttpHeaders headers = new HttpHeaders();

    @Test
    void createProductInventory(){
        headers.setBearerAuth(token);
        ProInvReqDto proInvReqDto=new ProInvReqDto();
        proInvReqDto.setProductCode(1061L);
        proInvReqDto.setQuantity(600L);
        proInvReqDto.setReorderThreshold(22L);
        HttpEntity<ProInvReqDto> requestEntity=new HttpEntity<>(proInvReqDto,headers);
        ResponseEntity<ProductInventory> response=testRestTemplate.exchange("/api/v1/inventory", HttpMethod.POST,requestEntity,ProductInventory.class);
        assertEquals(HttpStatus.OK,response.getStatusCode());
    }
    @Test
    void deleteProductInventory(){
        headers.setBearerAuth(token);
        HttpEntity<Void> requestEntity=new HttpEntity<>(headers);
        ResponseEntity<String> response=testRestTemplate.exchange("/api/v1/inventory/"+1060, HttpMethod.DELETE,requestEntity,String.class);
        assertEquals(HttpStatus.OK,response.getStatusCode());
    }
    @Test
    void isProductAvailable(){
        headers.setBearerAuth(token);
        HttpEntity<Void> requestEntity=new HttpEntity<>(headers);
        ResponseEntity<Boolean> response=testRestTemplate.exchange("/api/v1/inventory/"+1060+"/availability?quantity="+1, HttpMethod.GET,requestEntity,Boolean.class);
        assertEquals(HttpStatus.OK,response.getStatusCode());
        assertEquals(true,response.getBody());
    }
    @Test
    void isProductNotAvailable(){
        headers.setBearerAuth(token);
        HttpEntity<Void> requestEntity=new HttpEntity<>(headers);
        ResponseEntity<Boolean> response=testRestTemplate.exchange("/api/v1/inventory/"+1060+"/availability?quantity="+601, HttpMethod.GET,requestEntity,Boolean.class);
        assertEquals(HttpStatus.OK,response.getStatusCode());
        assertEquals(false,response.getBody());
    }
    @Test
    void reserveProduct(){
        headers.setBearerAuth(token);
        HttpEntity<Void> requestEntity=new HttpEntity<>(headers);
        ResponseEntity<String> response=testRestTemplate.exchange("/api/v1/inventory/"+1061+"/reserve?quantity="+2, HttpMethod.POST,requestEntity,String.class);
        assertEquals(HttpStatus.OK,response.getStatusCode());
    }
    @Test
    void updateStock(){
        headers.setBearerAuth(token);
        HttpEntity<Void> requestEntity=new HttpEntity<>(headers);
        ResponseEntity<String> response=testRestTemplate.exchange("/api/v1/inventory/"+1028+"/update?quantity="+2, HttpMethod.PUT,requestEntity,String.class);
        assertEquals(HttpStatus.OK,response.getStatusCode());
    }

    @Test
    void handleReturn(){
        headers.setBearerAuth(token);
        HttpEntity<Void> requestEntity=new HttpEntity<>(headers);
        ResponseEntity<String> response=testRestTemplate.exchange("/api/v1/inventory/"+1028+"/return?quantity="+2, HttpMethod.POST,requestEntity,String.class);
        assertEquals(HttpStatus.OK,response.getStatusCode());
    }
    @Test
    void getProductQuantity(){
        headers.setBearerAuth(token);
        HttpEntity<Void> requestEntity=new HttpEntity<>(headers);
        ResponseEntity<Long> response=testRestTemplate.exchange("/api/v1/inventory/"+1028+"/quantity", HttpMethod.GET,requestEntity,Long.class);
        assertEquals(HttpStatus.OK,response.getStatusCode());
    }
    @Test
    void updateProductQuantity(){
        headers.setBearerAuth(token);
        HttpEntity<Void> requestEntity=new HttpEntity<>(headers);
        ResponseEntity<Long> response=testRestTemplate.exchange("/api/v1/inventory/"+1060+"?quantity="+600, HttpMethod.PUT,requestEntity,Long.class);
        assertEquals(HttpStatus.OK,response.getStatusCode());
    }

}
