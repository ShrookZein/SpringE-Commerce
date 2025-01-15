package com.global.ecommerce.Entity.shopEntity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.global.ecommerce.Entity.User;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "order_items")
public class OrderItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;
    //Todo: @NotNull will give error with function createOrder in  OrderService
//    @NotNull
    @ManyToOne
    @JoinColumn(name = "order_id")
    @JsonIgnore
    private Order order;
    @Min(value = 1,message = "quantity must be greater than or equal 1")
    private Long quantity;

    private BigDecimal price;
}