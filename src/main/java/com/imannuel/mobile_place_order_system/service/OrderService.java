package com.imannuel.mobile_place_order_system.service;

import com.imannuel.mobile_place_order_system.dto.request.order.OrderRequest;
import com.imannuel.mobile_place_order_system.dto.response.order.OrderResponse;
import com.imannuel.mobile_place_order_system.entity.Order;
import org.springframework.data.domain.Page;

public interface OrderService {
    OrderResponse addOrder(OrderRequest orderRequest);

    Order findOrderById(String orderId);

    OrderResponse findOrderByIdResponse(String orderId);

    Page<OrderResponse> getAllOrderByCustomerId(Integer page, Integer size, String sortBy, String customerId);
}
