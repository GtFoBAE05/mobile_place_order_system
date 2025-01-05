package com.imannuel.mobile_place_order_system.service.impl;

import com.imannuel.mobile_place_order_system.constant.MessageConstants;
import com.imannuel.mobile_place_order_system.entity.Cart;
import com.imannuel.mobile_place_order_system.entity.Customer;
import com.imannuel.mobile_place_order_system.repository.CartRepository;
import com.imannuel.mobile_place_order_system.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {
    private final CartRepository cartRepository;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void createCustomerCart(Customer customer) {
        Cart cart = Cart.builder()
                .customer(customer)
                .build();
        cartRepository.saveAndFlush(cart);
    }

    @Override
    @Transactional(readOnly = true)
    public Cart getCart(Customer customer) {
        return cartRepository.findByCustomer(customer).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, MessageConstants.CART_NOT_DEFINED)
        );
    }
}
