package com.imannuel.mobile_place_order_system.service.impl;

import com.imannuel.mobile_place_order_system.constant.MessageConstants;
import com.imannuel.mobile_place_order_system.dto.mapper.OrderMapper;
import com.imannuel.mobile_place_order_system.dto.request.order.OrderRequest;
import com.imannuel.mobile_place_order_system.dto.response.order.OrderResponse;
import com.imannuel.mobile_place_order_system.entity.Customer;
import com.imannuel.mobile_place_order_system.entity.Order;
import com.imannuel.mobile_place_order_system.repository.OrderRepository;
import com.imannuel.mobile_place_order_system.service.CustomerService;
import com.imannuel.mobile_place_order_system.service.OrderItemService;
import com.imannuel.mobile_place_order_system.service.OrderService;
import com.imannuel.mobile_place_order_system.utility.FilteringSortingUtility;
import com.imannuel.mobile_place_order_system.utility.ValidationUtility;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final OrderItemService orderItemService;
    private final CustomerService customerService;
    private final ValidationUtility validationUtility;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public OrderResponse addOrder(OrderRequest orderRequest) {
        validationUtility.validate(orderRequest);

        Customer customer = customerService.findCustomerById(orderRequest.getCustomerId());

        Order order = Order.builder()
                .customer(customer)
                .grandTotal(0L)
                .orderItems(new ArrayList<>())
                .build();
        orderRepository.save(order);

        orderItemService.addOrderItemBulk(order, orderRequest.getCartItemId());

        return OrderMapper.toResponse(order);
    }

    @Override
    @Transactional(readOnly = true)
    public Order findOrderById(String orderId) {
        return orderRepository.findById(orderId).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, MessageConstants.ORDER_NOT_FOUND)
        );
    }

    @Override
    @Transactional(readOnly = true)
    public OrderResponse findOrderByIdResponse(String orderId) {
        Order order = findOrderById(orderId);
        return OrderMapper.toResponse(order);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<OrderResponse> getAllOrderByCustomerId(Integer page, Integer size, String sortBy, String customerId) {
        Customer customer = customerService.findCustomerById(customerId);
        Pageable pageable = FilteringSortingUtility.createPageable(page, size, sortBy);

        Page<Order> customerOrders = orderRepository.findAllByCustomer(customer, pageable);

        return customerOrders.map(
                OrderMapper::toResponse
        );
    }
}
