package com.imannuel.mobile_place_order_system.utility;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class FilterType {
    private String operator;

    private String value;
}
