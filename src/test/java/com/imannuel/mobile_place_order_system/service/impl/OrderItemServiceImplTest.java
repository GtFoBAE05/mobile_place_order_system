package com.imannuel.mobile_place_order_system.service.impl;

import com.imannuel.mobile_place_order_system.entity.CartItem;
import com.imannuel.mobile_place_order_system.entity.Order;
import com.imannuel.mobile_place_order_system.entity.OrderItem;
import com.imannuel.mobile_place_order_system.entity.Product;
import com.imannuel.mobile_place_order_system.repository.OrderItemRepository;
import com.imannuel.mobile_place_order_system.service.CartItemService;
import com.imannuel.mobile_place_order_system.service.ProductService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;


@ExtendWith(MockitoExtension.class)
class OrderItemServiceImplTest {
    @Mock
    private OrderItemRepository orderItemRepository;

    @Mock
    private ProductService productService;

    @Mock
    private CartItemService cartItemService;

    @InjectMocks
    private OrderItemServiceImpl orderItemService;

    @Test
    void shouldCallRepositoryWhenAddOrderItem() {
        Product product = Product.builder()
                .id(UUID.randomUUID().toString())
                .name("Macbook")
                .stock(9)
                .price(5000000L)
                .build();
        Order order = Order.builder()
                .id(UUID.randomUUID().toString())
                .grandTotal(5000000L)
                .orderItems(new ArrayList<>())
                .build();
        CartItem cartItem = CartItem.builder()
                .id(UUID.randomUUID().toString())
                .quantity(1)
                .product(product)
                .build();

        Mockito.doNothing().when(productService).hasEnoughStock(cartItem.getProduct().getId(), cartItem.getQuantity());
        Mockito.doNothing().when(cartItemService).deleteCartItem(cartItem.getId());
        Mockito.doNothing().when(productService).reduceProductStock(
                cartItem.getProduct().getId(), cartItem.getQuantity());

        orderItemService.addOrderItem(order, cartItem);

        Mockito.verify(orderItemRepository, Mockito.times(1)).saveAndFlush(Mockito.any(OrderItem.class));
    }

    @Test
    void shouldThrowErrorWhenAddOrderItemNotEnoughStock() {
        Product product = Product.builder()
                .id(UUID.randomUUID().toString())
                .name("Macbook")
                .stock(9)
                .price(5000000L)
                .build();
        Order order = Order.builder()
                .id(UUID.randomUUID().toString())
                .grandTotal(5000000L)
                .orderItems(new ArrayList<>())
                .build();
        CartItem cartItem = CartItem.builder()
                .id(UUID.randomUUID().toString())
                .quantity(1)
                .product(product)
                .build();

        Mockito.doThrow(new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                String.format("Not enough product stock for product: %s", product.getName())))
                .when(productService)
                .hasEnoughStock(cartItem.getProduct().getId(), cartItem.getQuantity());

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> orderItemService.addOrderItem(order, cartItem));
        assertEquals( String.format("Not enough product stock for product: %s", product.getName()),
                exception.getReason());
    }

    @Test
    void shouldAddOrderItemWhenAddOrderItemBulk(){
        Product product = Product.builder()
                .id(UUID.randomUUID().toString())
                .name("Macbook")
                .stock(9)
                .price(5000000L)
                .build();
        Order order = Order.builder()
                .id(UUID.randomUUID().toString())
                .grandTotal(5000000L)
                .orderItems(new ArrayList<>())
                .build();
        CartItem cartItem = CartItem.builder()
                .id(UUID.randomUUID().toString())
                .quantity(1)
                .product(product)
                .build();
        List<String> cartItemList = List.of(cartItem.getId());

        Mockito.when(cartItemService.findCartItemByIdAndCustomer(cartItem.getId(), order.getCustomer()))
                .thenReturn(cartItem);
        Mockito.doNothing().when(productService).hasEnoughStock(cartItem.getProduct().getId(), cartItem.getQuantity());
        Mockito.doNothing().when(cartItemService).deleteCartItem(cartItem.getId());
        Mockito.doNothing().when(productService).reduceProductStock(
                cartItem.getProduct().getId(), cartItem.getQuantity());

        orderItemService.addOrderItemBulk(order, cartItemList);

        Mockito.verify(orderItemRepository, Mockito.times(1))
                .saveAndFlush(Mockito.any(OrderItem.class));
    }

}