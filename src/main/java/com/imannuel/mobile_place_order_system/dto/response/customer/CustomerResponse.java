package com.imannuel.mobile_place_order_system.dto.response.customer;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class CustomerResponse {
    private String customerId;

    private String name;

    private String address;
}
