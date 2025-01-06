package com.imannuel.mobile_place_order_system.dto.response.product;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ProductTypeResponse {
    private Integer productTypeId;

    private String productTypeName;
}
