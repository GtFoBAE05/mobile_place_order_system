package com.imannuel.mobile_place_order_system.dto.mapper;

import com.imannuel.mobile_place_order_system.dto.response.order.OrderResponse;
import com.imannuel.mobile_place_order_system.entity.Order;

public class OrderMapper {
    public static OrderResponse toResponse(Order order) {
        return OrderResponse.builder()
                .orderId(order.getId())
                .customer(CustomerMapper.toResponse(order.getCustomer()))
                .items(order.getOrderItems().stream().map(
                        OrderItemMapper::toResponse
                ).toList())
                .grandTotal(order.getGrandTotal())
                .orderDate(order.getCreatedAt().toString())
                .build();
    }
}

