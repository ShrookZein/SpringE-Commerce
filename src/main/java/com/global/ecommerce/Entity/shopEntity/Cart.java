package com.global.ecommerce.Entity.shopEntity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.global.ecommerce.Entity.User;
import lombok.*;

import javax.persistence.*;
import java.util.List;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "carts")
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @OneToMany(mappedBy = "cart",cascade = CascadeType.ALL,orphanRemoval = true,fetch = FetchType.EAGER)
    private List<CartItem> cartItems;
    @Column(name = "user_id",nullable = false)
    private Long userId;
//    @OneToOne(cascade = CascadeType.ALL)
//    @JoinColumn(name = "user_id")
//    @JsonIgnore
//    private User user;
}
