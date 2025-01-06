package com.imannuel.mobile_place_order_system.dto.response.order;

import com.imannuel.mobile_place_order_system.dto.response.customer.CustomerResponse;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class OrderResponse {
    private String orderId;

    private CustomerResponse customer;

    private Long grandTotal;

    private List<OrderItemResponse> items;

    private String orderDate;
}
