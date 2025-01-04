package com.imannuel.mobile_place_order_system.dto.mapper;


import com.imannuel.mobile_place_order_system.dto.request.customer.CustomerRequest;
import com.imannuel.mobile_place_order_system.dto.response.customer.CustomerResponse;
import com.imannuel.mobile_place_order_system.entity.Customer;

public class CustomerMapper {
    public static Customer toEntity(CustomerRequest customerRequest) {
        return Customer.builder()
                .name(customerRequest.getName())
                .address(customerRequest.getAddress())
                .build();
    }

    public static CustomerResponse toResponse(Customer customer) {
        return CustomerResponse.builder()
                .id(customer.getId())
                .name(customer.getName())
                .address(customer.getAddress())
                .build();
    }
}
