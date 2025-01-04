package com.imannuel.mobile_place_order_system.dto.response.template;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class PaginationResponse {
    private Integer currentPage;
    private Integer size;
    private Integer totalPages;
    private Long totalResults;
}
