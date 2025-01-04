package com.imannuel.mobile_place_order_system.dto.request.product;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductTypeRequest {
    @NotBlank(message = "name is required")
    private String name;
}
