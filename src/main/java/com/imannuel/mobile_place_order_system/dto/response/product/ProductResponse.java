package com.imannuel.mobile_place_order_system.dto.response.product;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ProductResponse {
    private String id;

    private String name;

    private ProductTypeResponse productType;

    private Integer stock;

    private Long price;
}
