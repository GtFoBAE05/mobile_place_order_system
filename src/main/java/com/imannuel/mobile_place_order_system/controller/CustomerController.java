package com.imannuel.mobile_place_order_system.controller;

import com.imannuel.mobile_place_order_system.constant.ApiEndpointConstants;
import com.imannuel.mobile_place_order_system.constant.MessageConstants;
import com.imannuel.mobile_place_order_system.dto.mapper.ResponseMapper;
import com.imannuel.mobile_place_order_system.dto.request.customer.CustomerRequest;
import com.imannuel.mobile_place_order_system.dto.response.customer.CustomerResponse;
import com.imannuel.mobile_place_order_system.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = ApiEndpointConstants.CUSTOMER_ENDPOINT)
@RequiredArgsConstructor
public class CustomerController {
    private final CustomerService customerService;

    @PostMapping
    public ResponseEntity<?> createCustomer(
            @RequestBody CustomerRequest customerRequest
    ) {
        CustomerResponse customerResponse = customerService.addCustomer(customerRequest);
        return ResponseMapper.toCommonResponse(true, HttpStatus.CREATED,
                MessageConstants.CUSTOMER_SUCCESS_CREATE, customerResponse);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findCustomerById(
            @PathVariable String id
    ) {
        CustomerResponse customerResponse = customerService.findCustomerByIdResponse(id);
        return ResponseMapper.toCommonResponse(true, HttpStatus.OK,
                MessageConstants.CUSTOMER_SUCCESS_FIND, customerResponse);
    }

    @GetMapping()
    public ResponseEntity<?> getAllCustomer(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) String sortBy
    ) {
        Page<CustomerResponse> customerResponse = customerService.getAllCustomerResponse
                (page, size, sortBy);
        return ResponseMapper.toCommonResponseWithPagination(true, HttpStatus.OK,
                MessageConstants.CUSTOMER_SUCCESS_FETCH_ALL, customerResponse);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateCustomer(
            @PathVariable String id,
            @RequestBody CustomerRequest customerRequest
    ) {
        CustomerResponse customerResponse = customerService.updateCustomerById(id, customerRequest);
        return ResponseMapper.toCommonResponse(true, HttpStatus.OK,
                MessageConstants.CUSTOMER_SUCCESS_UPDATE, customerResponse);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCustomer(
            @PathVariable String id
    ) {
        customerService.deleteCustomer(id);
        return ResponseMapper.toCommonResponse(true, HttpStatus.OK,
                MessageConstants.CUSTOMER_SUCCESS_DELETE, null);
    }
}
