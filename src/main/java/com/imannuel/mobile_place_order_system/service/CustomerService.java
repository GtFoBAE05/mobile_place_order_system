package com.imannuel.mobile_place_order_system.service;

import com.imannuel.mobile_place_order_system.dto.request.customer.CustomerRequest;
import com.imannuel.mobile_place_order_system.dto.response.customer.CustomerResponse;
import com.imannuel.mobile_place_order_system.entity.Customer;
import org.springframework.data.domain.Page;

public interface CustomerService {
    CustomerResponse addCustomer(CustomerRequest customerRequest);

    Customer findCustomerById(String id);

    CustomerResponse findCustomerByIdResponse(String id);

    Page<CustomerResponse> getAllCustomerResponse(Integer page, Integer size, String sortBy);

    CustomerResponse updateCustomerById(String id, CustomerRequest customerRequest);

    void deleteCustomer(String id);
}
