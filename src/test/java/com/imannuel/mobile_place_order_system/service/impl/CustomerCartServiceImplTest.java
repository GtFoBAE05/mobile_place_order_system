package com.imannuel.mobile_place_order_system.service.impl;

import com.imannuel.mobile_place_order_system.dto.request.cart.CartRequest;
import com.imannuel.mobile_place_order_system.dto.response.cart.CartResponse;
import com.imannuel.mobile_place_order_system.entity.Cart;
import com.imannuel.mobile_place_order_system.entity.Customer;
import com.imannuel.mobile_place_order_system.service.CartItemService;
import com.imannuel.mobile_place_order_system.service.CartService;
import com.imannuel.mobile_place_order_system.service.CustomerService;
import com.imannuel.mobile_place_order_system.utility.ValidationUtility;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class CustomerCartServiceImplTest {
    @Mock
    private CustomerService customerService;

    @Mock
    private CartService cartService;

    @Mock
    private CartItemService cartItemService;

    @Mock
    private ValidationUtility validationUtility;

    @InjectMocks
    private CustomerCartServiceImpl customerCartService;

    @Test
    void shouldCallAnotherServiceWhenAddCartItem() {
        Customer customer = Customer.builder()
                .id(UUID.randomUUID().toString())
                .name("budi")
                .address("jalan ceria")
                .build();
        CartRequest cartRequest = CartRequest.builder()
                .productId(UUID.randomUUID().toString())
                .quantity(3)
                .build();
        Cart cart = Cart.builder()
                .id(UUID.randomUUID().toString())
                .customer(customer)
                .cartItems(List.of())
                .build();

        Mockito.doNothing().when(validationUtility).validate(cartRequest);
        Mockito.when(customerService.findCustomerById(customer.getId())).thenReturn(customer);
        Mockito.when(cartService.getCart(customer)).thenReturn(cart);

        customerCartService.addCartItem(customer.getId(), cartRequest);

        Mockito.verify(customerService, Mockito.times(1)).findCustomerById(Mockito.anyString());
        Mockito.verify(cartService, Mockito.times(1)).getCart(Mockito.any(Customer.class));
    }

    @Test
    void shouldReturnCartResponseWhenGetCustomerCart() {
        String cartId = UUID.randomUUID().toString();
        Customer customer = Customer.builder()
                .id(UUID.randomUUID().toString())
                .name("budi")
                .address("jalan ceria")
                .build();
        Cart cart = Cart.builder()
                .id(cartId)
                .customer(customer)
                .cartItems(List.of())
                .build();
        CartResponse expectedCartResponse = CartResponse.builder()
                .id(cartId)
                .items(List.of())
                .build();

        Mockito.when(customerService.findCustomerById(customer.getId())).thenReturn(customer);
        Mockito.when(cartService.getCart(customer)).thenReturn(cart);

        CartResponse customerCart = customerCartService.getCustomerCart(customer.getId());

        assertEquals(expectedCartResponse.getId(), customerCart.getId());
    }

    @Test
    void shouldUpdateCartItemWhenUpdateCartItem(){
        String cartId = UUID.randomUUID().toString();
        Customer customer = Customer.builder()
                .id(UUID.randomUUID().toString())
                .name("budi")
                .address("jalan ceria")
                .build();
        CartRequest cartRequest = CartRequest.builder()
                .productId(UUID.randomUUID().toString())
                .quantity(3)
                .build();
        Cart cart = Cart.builder()
                .id(cartId)
                .customer(customer)
                .cartItems(List.of())
                .build();

        Mockito.when(customerService.findCustomerById(customer.getId())).thenReturn(customer);
        Mockito.when(cartService.getCart(customer)).thenReturn(cart);
        Mockito.doNothing().when(cartItemService).updateCartItem(cart, cartRequest.getProductId(), cartRequest.getQuantity());

        customerCartService.updateCartItem(customer.getId(), cartRequest);

        Mockito.verify(cartItemService, Mockito.times(1)).updateCartItem(
                Mockito.any(Cart.class), Mockito.anyString(), Mockito.anyInt()
        );
    }

    @Test
    void shouldClearAllCartItemWhenClearAllCartItem(){
        String cartId = UUID.randomUUID().toString();
        Customer customer = Customer.builder()
                .id(UUID.randomUUID().toString())
                .name("budi")
                .address("jalan ceria")
                .build();
        Cart cart = Cart.builder()
                .id(cartId)
                .customer(customer)
                .cartItems(List.of())
                .build();

        Mockito.when(customerService.findCustomerById(Mockito.anyString())).thenReturn(customer);
        Mockito.when(cartService.getCart(customer)).thenReturn(cart);
        Mockito.doNothing().when(cartItemService).clearAllItemFromCart(cart);

        customerCartService.clearAllCartItem(cartId);

        Mockito.verify(cartItemService, Mockito.times(1)).clearAllItemFromCart(
                Mockito.any(Cart.class)
        );
    }

    @Test
    void shouldRemoveItemFromCartWhenRemoveItemFromCart(){
        String cartId = UUID.randomUUID().toString();
        Customer customer = Customer.builder()
                .id(UUID.randomUUID().toString())
                .name("budi")
                .address("jalan ceria")
                .build();
        Cart cart = Cart.builder()
                .id(cartId)
                .customer(customer)
                .cartItems(List.of())
                .build();
        String cartItemId = UUID.randomUUID().toString();

        Mockito.when(customerService.findCustomerById(Mockito.anyString())).thenReturn(customer);
        Mockito.when(cartService.getCart(customer)).thenReturn(cart);
        Mockito.doNothing().when(cartItemService).removeItemFromCart(cartItemId, cart);

        customerCartService.removeItemFromCart(customer.getId(), cartItemId);

        Mockito.verify(cartItemService, Mockito.times(1)).removeItemFromCart(
                Mockito.anyString(), Mockito.any()
        );
    }

}