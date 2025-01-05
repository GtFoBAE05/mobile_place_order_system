package com.imannuel.mobile_place_order_system.controller;

import com.imannuel.mobile_place_order_system.constant.ApiEndpointConstants;
import com.imannuel.mobile_place_order_system.constant.MessageConstants;
import com.imannuel.mobile_place_order_system.dto.mapper.ResponseMapper;
import com.imannuel.mobile_place_order_system.dto.request.order.OrderRequest;
import com.imannuel.mobile_place_order_system.dto.response.order.OrderResponse;
import com.imannuel.mobile_place_order_system.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = ApiEndpointConstants.ORDER_ENDPOINT)
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<?> createOrder(
            @RequestBody OrderRequest orderRequest
    ) {
        OrderResponse orderResponse = orderService.addOrder(orderRequest);
        return ResponseMapper.toCommonResponse(true, HttpStatus.CREATED, MessageConstants.ORDER_SUCCESS_CREATE, orderResponse);
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<?> getOrderDetail(
            @PathVariable(name = "orderId") String orderId
    ) {
        OrderResponse orderResponse = orderService.findOrderByIdResponse(orderId);
        return ResponseMapper.toCommonResponse(true, HttpStatus.OK, MessageConstants.ORDER_SUCCESS_FIND, orderResponse);
    }

    @GetMapping("/customer/{customerId}")
    public ResponseEntity<?> getCustomerOrder(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) String sortBy,
            @PathVariable(name = "customerId") String customerId
    ) {
        Page<OrderResponse> customerOrders = orderService.getAllOrderByCustomerId(page, size, sortBy, customerId);
        return ResponseMapper.toCommonResponseWithPagination(true, HttpStatus.OK, MessageConstants.ORDER_SUCCESS_FETCH_ALL, customerOrders);
    }
}
