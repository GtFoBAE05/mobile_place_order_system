package com.imannuel.mobile_place_order_system.dto.mapper;

import com.imannuel.mobile_place_order_system.dto.response.cart.CartItemResponse;
import com.imannuel.mobile_place_order_system.entity.CartItem;

public class CartItemMapper {
    public static CartItemResponse toResponse(CartItem cartItem) {
        return CartItemResponse.builder()
                .cartItemId(cartItem.getId())
                .productId(cartItem.getProduct().getId())
                .productName(cartItem.getProduct().getName())
                .productType(cartItem.getProduct().getProductType().getName())
                .quantity(cartItem.getQuantity())
                .price(cartItem.getProduct().getPrice())
                .totalPrice(cartItem.getProduct().getPrice() * cartItem.getQuantity())
                .build();
    }
}
