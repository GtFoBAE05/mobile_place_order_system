package com.imannuel.mobile_place_order_system.dto.response.cart;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class CartItemResponse {
    private String id;

    private String productId;

    private String productName;

    private String productType;

    private Integer quantity;

    private Long price;

    private Long totalPrice;
}
