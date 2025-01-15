package com.global.ecommerce.DTO.shopDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CartItemRequestDto {
    @NotNull(message = "Id of product is mandatory")
    private Long productId;
    @Min(value= 1,message = "quantity must be greater than or equal 1")
    private Long quantity;
}