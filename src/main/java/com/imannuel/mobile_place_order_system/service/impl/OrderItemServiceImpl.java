package com.imannuel.mobile_place_order_system.service.impl;

import com.imannuel.mobile_place_order_system.entity.CartItem;
import com.imannuel.mobile_place_order_system.entity.Order;
import com.imannuel.mobile_place_order_system.entity.OrderItem;
import com.imannuel.mobile_place_order_system.repository.OrderItemRepository;
import com.imannuel.mobile_place_order_system.service.CartItemService;
import com.imannuel.mobile_place_order_system.service.OrderItemService;
import com.imannuel.mobile_place_order_system.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderItemServiceImpl implements OrderItemService {
    private final OrderItemRepository orderItemRepository;
    private final ProductService productService;
    private final CartItemService cartItemService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addOrderItem(Order order, CartItem cartItem) {
        productService.hasEnoughStock(cartItem.getProduct().getId(), cartItem.getQuantity());

        OrderItem orderItem = buildOrderItem(order, cartItem);
        order.getOrderItems().add(orderItem);
        order.setGrandTotal(order.getGrandTotal() + orderItem.getTotalPrice());

        orderItemRepository.saveAndFlush(orderItem);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addOrderItemBulk(Order order, List<String> cartItemId) {
        for (String id : cartItemId) {
            CartItem cartItem = cartItemService.findCartItemByIdAndCustomer(id, order.getCustomer());
            addOrderItem(order, cartItem);
            cartItemService.deleteCartItem(id);
        }
    }

    private OrderItem buildOrderItem(Order order, CartItem cartItem) {
        return OrderItem.builder()
                .order(order)
                .product(cartItem.getProduct())
                .quantity(cartItem.getQuantity())
                .unitPrice(cartItem.getProduct().getPrice())
                .totalPrice(cartItem.getProduct().getPrice() * cartItem.getQuantity())
                .build();
    }

}
