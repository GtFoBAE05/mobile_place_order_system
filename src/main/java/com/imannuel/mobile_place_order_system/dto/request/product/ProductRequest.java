package com.imannuel.mobile_place_order_system.dto.request.product;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductRequest {
    @NotBlank(message = "name is required")
    private String name;

    @NotNull(message = "product type id is required")
    private Integer productTypeId;

    @NotNull(message = "stock is required")
    @Min(value = 0, message = "stock cant less than 0")
    private Integer stock;

    @NotNull(message = "price is required")
    @Min(value = 1, message = "price must be at least 1")
    private Long price;
}
