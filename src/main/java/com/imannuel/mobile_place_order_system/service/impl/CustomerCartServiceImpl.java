package com.imannuel.mobile_place_order_system.service.impl;

import com.imannuel.mobile_place_order_system.dto.mapper.CartMapper;
import com.imannuel.mobile_place_order_system.dto.request.cart.CartRequest;
import com.imannuel.mobile_place_order_system.dto.response.cart.CartResponse;
import com.imannuel.mobile_place_order_system.entity.Cart;
import com.imannuel.mobile_place_order_system.entity.Customer;
import com.imannuel.mobile_place_order_system.service.CartItemService;
import com.imannuel.mobile_place_order_system.service.CartService;
import com.imannuel.mobile_place_order_system.service.CustomerCartService;
import com.imannuel.mobile_place_order_system.service.CustomerService;
import com.imannuel.mobile_place_order_system.utility.ValidationUtility;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomerCartServiceImpl implements CustomerCartService {
    private final CustomerService customerService;
    private final CartService cartService;
    private final CartItemService cartItemService;
    private final ValidationUtility validationUtility;

    @Override
    public CartResponse addCartItem(String customerId, CartRequest cartRequest) {
        validationUtility.validate(cartRequest);

        Customer customer = customerService.findCustomerById(customerId);
        Cart cart = cartService.getCart(customer);
        cartItemService.addItemToCart(cart, cartRequest.getProductId(), cartRequest.getQuantity());

        return CartMapper.toResponse(cart);
    }

    @Override
    public CartResponse getCustomerCart(String customerId) {
        Customer customer = customerService.findCustomerById(customerId);
        Cart cart = cartService.getCart(customer);
        return CartMapper.toResponse(cart);
    }

    @Override
    public CartResponse updateCartItem(String customerId, CartRequest cartRequest) {
        Customer customer = customerService.findCustomerById(customerId);
        Cart cart = cartService.getCart(customer);
        cartItemService.updateCartItem(cart, cartRequest.getProductId(), cartRequest.getQuantity());

        return CartMapper.toResponse(cart);
    }

    @Override
    public void clearAllCartItem(String customerId) {
        Customer customer = customerService.findCustomerById(customerId);
        Cart cart = cartService.getCart(customer);
        cartItemService.clearAllItemFromCart(cart);
    }

    @Override
    public CartResponse removeItemFromCart(String customerId, String cartItemId) {
        Customer customer = customerService.findCustomerById(customerId);
        Cart cart = cartService.getCart(customer);
        cartItemService.removeItemFromCart(cartItemId, cart);

        return CartMapper.toResponse(cart);
    }
}
