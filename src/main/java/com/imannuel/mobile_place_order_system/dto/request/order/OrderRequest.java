package com.imannuel.mobile_place_order_system.dto.request.order;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class OrderRequest {
    @NotBlank(message = "customer id is required")
    private String customerId;

    @NotEmpty(message = "Cart item id cannot be empty")
    private List<String> cartItemId;
}
