package com.imannuel.mobile_place_order_system.dto.response.cart;

import com.imannuel.mobile_place_order_system.dto.response.customer.CustomerResponse;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class CartResponse {
    private String id;

    private CustomerResponse customer;

    private List<CartItemResponse> items;
}
