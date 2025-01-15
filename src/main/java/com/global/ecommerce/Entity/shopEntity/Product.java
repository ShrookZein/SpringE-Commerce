package com.global.ecommerce.Entity.shopEntity;

import lombok.*;
import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "products")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Name of product is mandatory")
    private String name;

    @NotBlank(message = "Description of product is mandatory")
    private String description;

    @Min(value = 0, message = "Price of product must be greater than or equal to 0")
    @Builder.Default
    private Double price = 0.0;

}