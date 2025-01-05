package com.imannuel.mobile_place_order_system.service;

import com.imannuel.mobile_place_order_system.entity.Cart;
import com.imannuel.mobile_place_order_system.entity.CartItem;
import com.imannuel.mobile_place_order_system.entity.Customer;

public interface CartItemService {
    void addItemToCart(Cart cart, String productId, Integer quantity);

    CartItem findCartItemById(String cartItemId);

    CartItem findCartItemByIdAndCustomer(String cartItemId, Customer customer);

    CartItem findCartItemByIdAndCart(String cartItemId, Cart cart);

    CartItem findCartItemByCartAndProduct(Cart cart, String productId);

    void updateCartItem(Cart cart, String productId, Integer quantity);

    void removeItemFromCart(String cartItemId, Cart cart);

    void clearAllItemFromCart(Cart cart);

    void deleteCartItem(String cartItemId);
}
