package com.imannuel.mobile_place_order_system.service.impl;

import com.imannuel.mobile_place_order_system.constant.MessageConstants;
import com.imannuel.mobile_place_order_system.dto.mapper.CustomerMapper;
import com.imannuel.mobile_place_order_system.dto.request.customer.CustomerRequest;
import com.imannuel.mobile_place_order_system.dto.response.customer.CustomerResponse;
import com.imannuel.mobile_place_order_system.entity.Customer;
import com.imannuel.mobile_place_order_system.repository.CustomerRepository;
import com.imannuel.mobile_place_order_system.service.CartService;
import com.imannuel.mobile_place_order_system.service.CustomerService;
import com.imannuel.mobile_place_order_system.utility.FilteringSortingUtility;
import com.imannuel.mobile_place_order_system.utility.ValidationUtility;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {
    private final CustomerRepository customerRepository;
    private final CartService cartService;
    private final ValidationUtility validationUtility;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public CustomerResponse addCustomer(CustomerRequest customerRequest) {
        validationUtility.validate(customerRequest);

        Customer customer = CustomerMapper.toEntity(customerRequest);
        customerRepository.saveAndFlush(customer);
        cartService.createCustomerCart(customer);

        return CustomerMapper.toResponse(customer);
    }

    @Override
    @Transactional(readOnly = true)
    public Customer findCustomerById(String id) {
        return customerRepository.findById(id).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, MessageConstants.CUSTOMER_NOT_FOUND));
    }

    @Override
    @Transactional(readOnly = true)
    public CustomerResponse findCustomerByIdResponse(String id) {
        Customer customer = findCustomerById(id);
        return CustomerMapper.toResponse(customer);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CustomerResponse> getAllCustomerResponse(Integer page, Integer size, String sortBy) {
        Pageable pageable = FilteringSortingUtility.createPageable(page, size, sortBy);

        Page<Customer> customers = customerRepository.findAll(pageable);

        return customers.map(CustomerMapper::toResponse);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public CustomerResponse updateCustomerById(String id, CustomerRequest customerRequest) {
        validationUtility.validate(customerRequest);

        Customer customer = findCustomerById(id);
        customer.setName(customerRequest.getName());
        customer.setAddress(customerRequest.getAddress());
        customerRepository.saveAndFlush(customer);

        return CustomerMapper.toResponse(customer);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteCustomer(String id) {
        Customer customer = findCustomerById(id);
        customerRepository.delete(customer);
    }
}
