package com.imannuel.mobile_place_order_system.dto.mapper;

import com.imannuel.mobile_place_order_system.dto.response.cart.CartResponse;
import com.imannuel.mobile_place_order_system.entity.Cart;

public class CartMapper {
    public static CartResponse toResponse(Cart cart) {
        return CartResponse.builder()
                .id(cart.getId())
                .customer(CustomerMapper.toResponse(cart.getCustomer()))
                .items(cart.getCartItems().stream().map(
                        CartItemMapper::toResponse
                ).toList())
                .build();
    }
}
