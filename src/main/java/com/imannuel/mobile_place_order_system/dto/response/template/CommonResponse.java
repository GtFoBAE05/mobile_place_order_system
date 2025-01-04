package com.imannuel.mobile_place_order_system.dto.response.template;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class CommonResponse<T> {
    private boolean success;
    private String message;
    private T data;
    private PaginationResponse pagination;
}
