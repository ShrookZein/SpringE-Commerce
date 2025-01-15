package com.global.ecommerce.Entity.inventoryEntity;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Setter
@Getter
@Entity
@Builder
@AllArgsConstructor
//@NoArgsConstructor
@Table(name = "product_inventory")
public class ProductInventory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Column(unique = true, nullable = false)
    @NotNull
    private Long productCode;

    @NotNull
    @Min(value = 0,message = "quantity must be greater than or equal to 0")
    private Long quantity;

    @Min(value = 0, message = "Reorder threshold must be greater than or equal to 0")
    private Long reorderThreshold;
    @Min(value = 0, message = "reserved Quantity must be greater than or equal to 0")
    @Builder.Default
    @Column(nullable = false, columnDefinition = "BIGINT DEFAULT 0")
    private Long reservedQuantity = 0L;
    public ProductInventory() {
        this.reservedQuantity = 0L;
    }
}
