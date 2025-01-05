package com.imannuel.mobile_place_order_system.service.impl;

import com.imannuel.mobile_place_order_system.constant.MessageConstants;
import com.imannuel.mobile_place_order_system.entity.Cart;
import com.imannuel.mobile_place_order_system.entity.Customer;
import com.imannuel.mobile_place_order_system.repository.CartRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class CartServiceImplTest {
    @Mock
    private CartRepository cartRepository;

    @InjectMocks
    private CartServiceImpl cartService;

    @Test
    void shouldCallCartRepositoryWhenCreateCustomerCart(){
        Customer customer = Customer.builder()
                .id(UUID.randomUUID().toString())
                .name("budi")
                .address("jalan ceria")
                .build();

        cartService.createCustomerCart(customer);

        Mockito.verify(cartRepository, Mockito.times(1))
                .saveAndFlush(Mockito.any(Cart.class));
    }

    @Test
    void shouldReturnCartWhenGetCart() {
        Customer customer = Customer.builder()
            .id(UUID.randomUUID().toString())
            .name("budi")
            .address("jalan ceria")
            .build();
        Cart expectedCart = Cart.builder().customer(customer).build();

        Mockito.when(cartRepository.findByCustomer(customer)).thenReturn(Optional.of(expectedCart));

        Cart actualCart = cartService.getCart(customer);

        assertEquals(expectedCart, actualCart);
        Mockito.verify(cartRepository, Mockito.times(1)).findByCustomer(customer);
    }

    @Test
    void shouldThrowErrorWhenGetCartNotFound() {
        Customer customer = Customer.builder()
            .id(UUID.randomUUID().toString())
            .name("budi")
            .address("jalan ceria")
            .build();

        Mockito.when(cartRepository.findByCustomer(customer)).thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> cartService.getCart(customer));

        assertEquals(MessageConstants.CART_NOT_DEFINED, exception.getReason());
        Mockito.verify(cartRepository, Mockito.times(1)).findByCustomer(customer);
    }

}