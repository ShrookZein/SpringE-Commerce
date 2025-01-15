package com.global.ecommerce.controller.shopControllerTest;

import com.global.ecommerce.DTO.shopDto.CartItemRequestDto;
import com.global.ecommerce.DTO.shopDto.ProductRequestDto;
import com.global.ecommerce.DTO.shopDto.ProductResponseDto;
import com.global.ecommerce.Entity.shopEntity.Cart;
import com.global.ecommerce.Entity.shopEntity.CartItem;
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
public class CartControllerTest {
    @Autowired
    private TestRestTemplate testRestTemplate;

    //Enter Valid Token
    String token="eyJhbGciOiJIUzUxMiJ9.eyJqdGkiOiIyN2YyZjMxMS1lOThhLTQwODYtODFmOC05MDc3MmE3N2UwZjkiLCJzdWIiOiJTaHJvb2tra2tAZ21haWwuY29tIiwiaWF0IjoxNzM2ODUxMzU1LCJpc3MiOiJhcHAtU2VydmljZSIsImV4cCI6MTczNzAzMTM1NSwiY3JlYXRlZCI6MTczNjg1MTM1NTM0MX0.5nT4xiFNRECvcoWpsEzQFG26G69klLVNAWUAlv2y1umLRhPx5sOzevxoRxLbcJr_Q-EafK_ZGNYMPWbJYt-qww";
    HttpHeaders headers = new HttpHeaders();
    @Test
    void addItemToCart(){
        headers.setBearerAuth(token);
        CartItemRequestDto cartItemRequestDto=new CartItemRequestDto();
        cartItemRequestDto.setProductId(1028L);
        cartItemRequestDto.setQuantity(2L);
        HttpEntity<CartItemRequestDto> requestEntity=new HttpEntity<>(cartItemRequestDto,headers);
        ResponseEntity<Cart> response=testRestTemplate.exchange("/api/v1/carts/", HttpMethod.POST,requestEntity,Cart.class);
        assertEquals(HttpStatus.OK,response.getStatusCode());
//        assertEquals(22.0, Objects.requireNonNull(response.getBody().getCartItems().get(0).getQuantity());
    }
    @Test
    void getItemsByUserId(){
        headers.setBearerAuth(token);
        HttpEntity<Void>requestEntity=new HttpEntity<>(headers);
        ResponseEntity<List<CartItem>>response=testRestTemplate.exchange("/api/v1/carts/" , HttpMethod.GET, requestEntity, new ParameterizedTypeReference<>() {});
        assertEquals(HttpStatus.OK,response.getStatusCode());
    }
    @Test
    void deleteAllItemsInCart(){
        headers.setBearerAuth(token);
        HttpEntity<Void>requestEntity=new HttpEntity<>(headers);
        ResponseEntity<Cart>response=testRestTemplate.exchange("/api/v1/carts/",HttpMethod.DELETE,requestEntity,Cart.class);
        assertEquals(HttpStatus.OK,response.getStatusCode());
    }
    @Test
    void deleteItemFromCart(){
        headers.setBearerAuth(token);
        HttpEntity<Void>requestEntity=new HttpEntity<>(headers);
        ResponseEntity<Cart>response=testRestTemplate.exchange("/api/v1/carts/item/"+1028L,HttpMethod.DELETE,requestEntity,Cart.class);
        assertEquals(HttpStatus.OK,response.getStatusCode());
    }
    @Test
    void updateCartItem(){
        headers.setBearerAuth(token);
        CartItemRequestDto cartItemRequestDto=new CartItemRequestDto();
        cartItemRequestDto.setQuantity(8L);
        cartItemRequestDto.setProductId(1028L);
        HttpEntity<CartItemRequestDto>requestEntity=new HttpEntity<>(cartItemRequestDto,headers);
        ResponseEntity<Cart>response=testRestTemplate.exchange("/api/v1/carts/item/",HttpMethod.PUT,requestEntity,Cart.class);
        assertEquals(HttpStatus.OK,response.getStatusCode());
    }

}
