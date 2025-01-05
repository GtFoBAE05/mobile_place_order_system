package com.imannuel.mobile_place_order_system.dto.mapper;

import com.imannuel.mobile_place_order_system.dto.response.order.OrderItemResponse;
import com.imannuel.mobile_place_order_system.entity.OrderItem;

public class OrderItemMapper {
    public static OrderItemResponse toResponse(OrderItem orderItem) {
        return OrderItemResponse.builder()
                .productId(orderItem.getProduct().getId())
                .productName(orderItem.getProduct().getName())
                .quantity(orderItem.getQuantity())
                .unitPrice(orderItem.getProduct().getPrice())
                .totalPrice(orderItem.getProduct().getPrice() * orderItem.getQuantity())
                .build();
    }
}
