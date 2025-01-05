package com.imannuel.mobile_place_order_system.service.impl;

import com.imannuel.mobile_place_order_system.constant.MessageConstants;
import com.imannuel.mobile_place_order_system.dto.request.order.OrderRequest;
import com.imannuel.mobile_place_order_system.dto.response.order.OrderResponse;
import com.imannuel.mobile_place_order_system.entity.Customer;
import com.imannuel.mobile_place_order_system.entity.Order;
import com.imannuel.mobile_place_order_system.entity.OrderItem;
import com.imannuel.mobile_place_order_system.entity.Product;
import com.imannuel.mobile_place_order_system.repository.OrderRepository;
import com.imannuel.mobile_place_order_system.service.CustomerService;
import com.imannuel.mobile_place_order_system.service.OrderItemService;
import com.imannuel.mobile_place_order_system.utility.ValidationUtility;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;


@ExtendWith(MockitoExtension.class)
class OrderServiceImplTest {
    @Mock
    private OrderRepository orderRepository;

    @Mock
    private OrderItemService orderItemService;

    @Mock
    private CustomerService customerService;

    @Mock
    private ValidationUtility validationUtility;

    @InjectMocks
    private OrderServiceImpl orderService;

    @Test
    void shouldCallRepositoryWhenAddOrder() {
        Customer customer = Customer.builder()
                .id(UUID.randomUUID().toString())
                .name("budi")
                .address("jalan ceria")
                .build();
        String cartItemId = UUID.randomUUID().toString();
        OrderRequest orderRequest = OrderRequest.builder()
                .customerId(customer.getId())
                .cartItemId(List.of(cartItemId))
                .build();
        Order order = Order.builder()
                .customer(customer)
                .grandTotal(0L)
                .orderItems(new ArrayList<>())
                .build();

        Mockito.when(customerService.findCustomerById(orderRequest.getCustomerId())).thenReturn(customer);
        Mockito.when(orderRepository.save(Mockito.any(Order.class))).thenReturn(order);
        Mockito.doNothing().when(orderItemService).addOrderItemBulk(Mockito.any(Order.class), Mockito.anyList());

        orderService.addOrder(orderRequest);

        Mockito.verify(orderRepository, Mockito.times(1)).save(Mockito.any(Order.class));
    }

    @Test
    void shouldReturnOrderWhenFindOrderById() {
        String orderId = UUID.randomUUID().toString();
        Order expectedOrder = Order.builder()
                .id(orderId)
                .build();

        Mockito.when(orderRepository.findById(orderId)).thenReturn(Optional.of(expectedOrder));

        Order order = orderService.findOrderById(orderId);

        assertEquals(orderId, order.getId());
        Mockito.verify(orderRepository, Mockito.times(1)).findById(Mockito.anyString());
    }

    @Test
    void shouldThrowErrorWhenFindOrderById() {
        String orderId = UUID.randomUUID().toString();

        Mockito.when(orderRepository.findById(orderId)).thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> orderService.findOrderById(orderId));

        assertEquals(MessageConstants.ORDER_NOT_FOUND, exception.getReason());
        Mockito.verify(orderRepository, Mockito.times(1)).findById(Mockito.anyString());
    }

    @Test
    void shouldReturnOrderResponseWhenFindOrderByIdResponse() {
        Customer customer = Customer.builder()
                .id(UUID.randomUUID().toString())
                .name("budi")
                .address("jalan ceria")
                .build();
        String orderId = UUID.randomUUID().toString();
        Order expectedOrder = Order.builder()
                .id(orderId)
                .customer(customer)
                .orderItems(List.of())
                .build();

        Mockito.when(orderRepository.findById(orderId)).thenReturn(Optional.of(expectedOrder));

        OrderResponse orderResponse = orderService.findOrderByIdResponse(orderId);

        assertEquals(expectedOrder.getId(), orderResponse.getOrderId());
        Mockito.verify(orderRepository, Mockito.times(1)).findById(Mockito.anyString());
    }

    @Test
    void shouldReturnPageOrderResponseWhenGetAllOrderByCustomerId() {
        int page = 1;
        int size = 10;
        String sortBy = "id_asc";
        Customer customer = Customer.builder()
                .id(UUID.randomUUID().toString())
                .name("budi")
                .address("jalan ceria")
                .build();
        Product product = Product.builder()
                .id(UUID.randomUUID().toString())
                .name("Macbook")
                .stock(20)
                .price(5000000L)
                .deleted(false)
                .build();
        OrderItem orderItem = OrderItem.builder()
                .id(UUID.randomUUID().toString())
                .product(product)
                .quantity(1)
                .build();
        String orderId = UUID.randomUUID().toString();
        Order order = Order.builder()
                .id(orderId)
                .customer(customer)
                .grandTotal(5000000L)
                .orderItems(List.of(orderItem))
                .build();

        Page<Order> expectedCustomerPage = new PageImpl<>(List.of(order));

        Mockito.when(customerService.findCustomerById(customer.getId())).thenReturn(customer);
        Mockito.when(orderRepository.findAllByCustomer(Mockito.any(Customer.class), Mockito.any(Pageable.class)))
                .thenReturn(expectedCustomerPage);

        Page<OrderResponse> orderResponsesPage = orderService
                .getAllOrderByCustomerId(page, size, sortBy, customer.getId());

        assertEquals(expectedCustomerPage.getContent().size(), orderResponsesPage.getContent().size());
        Mockito.verify(orderRepository, Mockito.times(1))
                .findAllByCustomer(Mockito.any(Customer.class), Mockito.any(Pageable.class));
    }
}