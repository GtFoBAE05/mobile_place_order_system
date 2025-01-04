package com.imannuel.mobile_place_order_system.dto.request.product;

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
    private Integer stock;

    @NotNull(message = "price is required")
    private Long price;
}
