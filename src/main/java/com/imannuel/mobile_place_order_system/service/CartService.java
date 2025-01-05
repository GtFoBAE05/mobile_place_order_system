package com.imannuel.mobile_place_order_system.service;

import com.imannuel.mobile_place_order_system.entity.Cart;
import com.imannuel.mobile_place_order_system.entity.Customer;

public interface CartService {
    void createCustomerCart(Customer customer);

    Cart getCart(Customer customer);
}
