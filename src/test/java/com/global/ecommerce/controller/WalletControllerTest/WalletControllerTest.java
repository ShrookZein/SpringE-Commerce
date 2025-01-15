package com.global.ecommerce.controller.WalletControllerTest;

import com.global.ecommerce.Entity.shopEntity.Cart;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;

import java.math.BigDecimal;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class WalletControllerTest {
    @Autowired
    private TestRestTemplate testRestTemplate;

    //Enter Valid Token
    String token="eyJhbGciOiJIUzUxMiJ9.eyJqdGkiOiIyN2YyZjMxMS1lOThhLTQwODYtODFmOC05MDc3MmE3N2UwZjkiLCJzdWIiOiJTaHJvb2tra2tAZ21haWwuY29tIiwiaWF0IjoxNzM2ODUxMzU1LCJpc3MiOiJhcHAtU2VydmljZSIsImV4cCI6MTczNzAzMTM1NSwiY3JlYXRlZCI6MTczNjg1MTM1NTM0MX0.5nT4xiFNRECvcoWpsEzQFG26G69klLVNAWUAlv2y1umLRhPx5sOzevxoRxLbcJr_Q-EafK_ZGNYMPWbJYt-qww";
    HttpHeaders headers = new HttpHeaders();
    @Test
    void deposit(){
        headers.setBearerAuth(token);
        HttpEntity<Void> requestEntity=new HttpEntity<>(headers);
        ResponseEntity<Map<String, Object>> response = testRestTemplate.exchange(
                "/api/v1/wallet/deposit/"+500.0,
                HttpMethod.POST,
                requestEntity,
                new ParameterizedTypeReference<Map<String, Object>>() {}
        );
        assertEquals(HttpStatus.OK,response.getStatusCode());
    }
    @Test
    void withdraw(){
        headers.setBearerAuth(token);
        HttpEntity<Void> requestEntity=new HttpEntity<>(headers);
        ResponseEntity<Map<String, Object>> response = testRestTemplate.exchange(
                "/api/v1/wallet/withdraw/"+50.0,
                HttpMethod.POST,
                requestEntity,
                new ParameterizedTypeReference<Map<String, Object>>() {}
        );
        assertEquals(HttpStatus.OK,response.getStatusCode());
    }
    @Test
    void getWalletBalance(){
        headers.setBearerAuth(token);
        HttpEntity<Void> requestEntity=new HttpEntity<>(headers);
        ResponseEntity<BigDecimal> response = testRestTemplate.exchange(
                "/api/v1/wallet/balance",
                HttpMethod.GET,
                requestEntity,
                BigDecimal.class
        );
        assertEquals(HttpStatus.OK,response.getStatusCode());
    }
}
