package com.global.ecommerce.DTO.inventoryDto;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Setter
@Getter
public class ProInvReqDto {
    @NotNull
    private Long productCode;
    @NotNull
    @Min(value = 0,message = "quantity must be greater than or equal to 0")
    private Long quantity;
    @Min(value = 0, message = "Reorder threshold must be greater than or equal to 0")
    private Long reorderThreshold;
}
