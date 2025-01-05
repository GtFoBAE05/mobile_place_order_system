package com.imannuel.mobile_place_order_system.service;

import com.imannuel.mobile_place_order_system.entity.CartItem;
import com.imannuel.mobile_place_order_system.entity.Order;

import java.util.List;

public interface OrderItemService {
    void addOrderItem(Order order, CartItem cartItem);

    void addOrderItemBulk(Order order, List<String> cartItemId);
}
