package com.imannuel.mobile_place_order_system.dto.response.order;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class OrderItemResponse {
    private String productId;

    private String productName;

    private Integer quantity;

    private Long unitPrice;

    private Long totalPrice;
}
