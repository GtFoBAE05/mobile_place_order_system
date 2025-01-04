package com.imannuel.mobile_place_order_system.utility;

import com.imannuel.mobile_place_order_system.constant.MessageConstants;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ResponseStatusException;

import java.util.Arrays;
import java.util.List;

public class FilteringSortingUtility {
    private static final List<String> listOfFilterOperator = List.of("e", "lt", "lte", "gt", "gte");

    public static Pageable createPageable(Integer page, Integer size, String sortBy) {
        Sort sort = Sort.unsorted();

        if (StringUtils.hasText(sortBy)) {
            sort = FilteringSortingUtility.parseMultipleSort(sortBy);
        }

        if (page <= 1) {
            page = 0;
        } else {
            page = page - 1;
        }
        return PageRequest.of(page, size, sort);
    }

    public static FilterType parseFilter(String filterBy) {
        String[] filterField = filterBy.split(":");
        if (filterField.length != 2) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, MessageConstants.INVALID_FILTER_FORMAT);
        }

        String operator = filterField[0];
        String value = filterField[1];

        if (!listOfFilterOperator.contains(operator)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, MessageConstants.INVALID_OPERATOR_TYPE);
        }

        return FilterType.builder()
                .operator(operator)
                .value(value)
                .build();
    }

    public static List<FilterType> parseMultipleFilter(String filterBy) {
        return Arrays.stream(filterBy.split(","))
                .map(FilteringSortingUtility::parseFilter)
                .toList();
    }

    public static Sort.Order parseSort(String sortBy) {
        String[] sortField = sortBy.split("_");

        if (sortField.length != 2) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, MessageConstants.INVALID_SORT_FORMAT);
        }

        String field = sortField[0];
        String direction = sortField[1];

        if (!direction.equalsIgnoreCase("asc") && !direction.equalsIgnoreCase("desc")) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, MessageConstants.INVALID_SORT_DIRECTION);
        }

        return direction.equalsIgnoreCase("asc")
                ? Sort.Order.asc(field)
                : Sort.Order.desc(field);
    }

    public static Sort parseMultipleSort(String sortBy) {
        List<Sort.Order> orders = Arrays.stream(sortBy.split(","))
                .map(FilteringSortingUtility::parseSort)
                .toList();
        return Sort.by(orders);
    }
}
