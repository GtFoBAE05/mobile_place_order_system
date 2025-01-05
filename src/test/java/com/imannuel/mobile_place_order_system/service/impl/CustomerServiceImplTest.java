package com.imannuel.mobile_place_order_system.service.impl;

import com.imannuel.mobile_place_order_system.constant.MessageConstants;
import com.imannuel.mobile_place_order_system.dto.request.customer.CustomerRequest;
import com.imannuel.mobile_place_order_system.dto.response.customer.CustomerResponse;
import com.imannuel.mobile_place_order_system.entity.Customer;
import com.imannuel.mobile_place_order_system.repository.CustomerRepository;
import com.imannuel.mobile_place_order_system.service.CartService;
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

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
class CustomerServiceImplTest {
    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private CartService cartService;

    @Mock
    private ValidationUtility validationUtility;

    @InjectMocks
    private CustomerServiceImpl customerService;

    @Test
    void shouldCallCustomerRepositoryWhenAddCustomer() {
        CustomerRequest customerRequest = CustomerRequest.builder()
                .name("budi")
                .address("jalan ceria")
                .build();

        Mockito.doNothing().when(validationUtility).validate(customerRequest);

        customerService.addCustomer(customerRequest);

        Mockito.verify(customerRepository, Mockito.times(1))
                .saveAndFlush(Mockito.any(Customer.class));
    }

    @Test
    void shouldReturnCustomerWhenFindCustomerById() {
        String customerId = UUID.randomUUID().toString();
        Customer expectedCustomer = Customer.builder()
                .id(customerId)
                .name("budi")
                .address("jalan ceria")
                .build();

        Mockito.when(customerRepository.findById(customerId)).thenReturn(Optional.of(expectedCustomer));

        Customer customer = customerService.findCustomerById(customerId);

        assertEquals(expectedCustomer, customer);
        Mockito.verify(customerRepository, Mockito.times(1)).findById(Mockito.anyString());
    }

    @Test
    void shouldThrowErrorWhenFindCustomerById() {
        String customerId = UUID.randomUUID().toString();

        Mockito.when(customerRepository.findById(customerId)).thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> customerService.findCustomerById(customerId));

        assertEquals(MessageConstants.CUSTOMER_NOT_FOUND, exception.getReason());
        Mockito.verify(customerRepository, Mockito.times(1)).findById(Mockito.anyString());
    }

    @Test
    void shouldReturnCustomerResponseWhenFindCustomerByIdResponse() {
        String customerId = UUID.randomUUID().toString();
        Customer expectedCustomer = Customer.builder()
                .id(customerId)
                .name("budi")
                .address("jalan ceria")
                .build();

        Mockito.when(customerRepository.findById(customerId)).thenReturn(Optional.of(expectedCustomer));

        CustomerResponse customerResponse = customerService.findCustomerByIdResponse(customerId);

        assertEquals(expectedCustomer.getId(), customerResponse.getId());
        assertEquals(expectedCustomer.getName(), customerResponse.getName());
        assertEquals(expectedCustomer.getAddress(), customerResponse.getAddress());
        Mockito.verify(customerRepository, Mockito.times(1)).findById(Mockito.anyString());
    }

    @Test
    void shouldThrowErrorWhenFindCustomerByIdResponse() {
        String customerId = UUID.randomUUID().toString();

        Mockito.when(customerRepository.findById(customerId)).thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> customerService.findCustomerByIdResponse(customerId));

        assertEquals(MessageConstants.CUSTOMER_NOT_FOUND, exception.getReason());
        Mockito.verify(customerRepository, Mockito.times(1)).findById(Mockito.anyString());
    }

    @Test
    void shouldReturnPageCustomerResponseWhenGetAllCustomerResponse() {
        int page = 1;
        int size = 10;
        String sortBy = "name_asc";

        Customer customer = Customer.builder()
                .id(UUID.randomUUID().toString())
                .name("budi")
                .address("jalan ceria")
                .build();
        Page<Customer> expectedCustomerPage = new PageImpl<>(List.of(customer));


        Mockito.when(customerRepository.findAll(Mockito.any(Pageable.class))).thenReturn(expectedCustomerPage);

        Page<CustomerResponse> customerResponsePage = customerService.getAllCustomerResponse(page, size, sortBy);

        assertEquals(expectedCustomerPage.getContent().size(), customerResponsePage.getContent().size());
        Mockito.verify(customerRepository, Mockito.times(1)).findAll(Mockito.any(Pageable.class));
    }

    @Test
    void shouldRetunCustomerResponseWhenUpdateCustomerById() {
        String customerId = UUID.randomUUID().toString();
        CustomerRequest customerRequest = CustomerRequest.builder()
                .name("budi123")
                .address("jalan ceria123")
                .build();
        Customer expectedCustomer = Customer.builder()
                .id(customerId)
                .name("budi")
                .address("jalan ceria")
                .build();

        Mockito.when(customerRepository.findById(customerId)).thenReturn(Optional.of(expectedCustomer));

        CustomerResponse customerResponse = customerService.updateCustomerById(customerId, customerRequest);

        assertEquals(customerRequest.getName(), customerResponse.getName());
        assertEquals(customerRequest.getAddress(), customerResponse.getAddress());
        Mockito.verify(customerRepository, Mockito.times(1)).findById(Mockito.anyString());
    }
}