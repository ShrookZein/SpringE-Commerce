package com.global.ecommerce.Entity.shopEntity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.global.ecommerce.Entity.User;
import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @OneToMany(mappedBy = "order",cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<OrderItem> orderItems;

    private BigDecimal totalAmount;

//    private LocalDateTime createdAt;
    @Column(name = "created_at", nullable = false)
    private Timestamp createdAt;
    private String status; // e.g., "PENDING", "COMPLETED", "CANCELLED"
}
