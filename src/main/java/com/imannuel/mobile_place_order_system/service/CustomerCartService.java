package com.imannuel.mobile_place_order_system.service;

import com.imannuel.mobile_place_order_system.dto.request.cart.CartRequest;
import com.imannuel.mobile_place_order_system.dto.response.cart.CartResponse;

public interface CustomerCartService {
    CartResponse addCartItem(String customerId, CartRequest cartRequest);

    CartResponse getCustomerCart(String customerId);

    CartResponse updateCartItem(String customerId, CartRequest cartRequest);

    void clearAllCartItem(String customerId);

    void removeItemFromCart(String customerId, String cartItemId);
}
