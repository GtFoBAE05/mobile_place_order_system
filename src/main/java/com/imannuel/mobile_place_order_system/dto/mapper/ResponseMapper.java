package com.imannuel.mobile_place_order_system.dto.mapper;

import com.imannuel.mobile_place_order_system.dto.response.template.CommonResponse;
import com.imannuel.mobile_place_order_system.dto.response.template.PaginationResponse;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class ResponseMapper {
    public static <T> ResponseEntity<?> toCommonResponse(Boolean success, HttpStatus httpStatus, String message, T data) {
        return ResponseEntity.status(httpStatus).body(
                CommonResponse.builder()
                        .success(success)
                        .message(message)
                        .data(data)
                        .pagination(null)
                        .build()
        );
    }

    public static ResponseEntity<?> toCommonResponseWithPagination(Boolean success, HttpStatus httpStatus, String message, Page<?> page) {
        PaginationResponse paginationResponse = PaginationResponse.builder()
                .currentPage(page.getNumber()+1)
                .size(page.getSize())
                .totalPages(page.getTotalPages())
                .totalResults(page.getTotalElements())
                .build();

        return ResponseEntity.status(httpStatus).body(
                CommonResponse.builder()
                        .success(success)
                        .message(message)
                        .data(page.getContent())
                        .pagination(paginationResponse)
                        .build()
        );
    }
}
