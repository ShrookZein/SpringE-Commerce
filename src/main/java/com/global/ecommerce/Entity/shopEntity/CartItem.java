package com.global.ecommerce.Entity.shopEntity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.springframework.stereotype.Service;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "cart_items")
public class CartItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull
    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;
//    @ManyToOne(cascade = CascadeType.ALL)
//    @NotNull
    @ManyToOne()
    @JoinColumn(name = "cart_id")
    @JsonIgnore
    private Cart cart;
    @Min(value = 1,message = "quantity must be greater than  or equal 1")
    @Builder.Default
    private Long quantity=0L;
}
