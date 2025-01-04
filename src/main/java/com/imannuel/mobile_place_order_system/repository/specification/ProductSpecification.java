package com.imannuel.mobile_place_order_system.repository.specification;

import com.imannuel.mobile_place_order_system.constant.MessageConstants;
import com.imannuel.mobile_place_order_system.entity.Product;
import com.imannuel.mobile_place_order_system.utility.FilterType;
import com.imannuel.mobile_place_order_system.utility.FilteringSortingUtility;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

public class ProductSpecification {
    public static Specification<Product> getSpecification(String name, Integer productTypeId, String price, String stock) {
        Specification<Product> specifications = Specification.where(null);

        if (StringUtils.hasText(name)) {
            specifications = specifications.and(ProductSpecification.hasName(name));
        }

        if (productTypeId != null) {
            specifications = specifications.and(ProductSpecification.hasProductTypeId(productTypeId));
        }

        if (StringUtils.hasText(price)) {
            List<FilterType> priceFilter = FilteringSortingUtility.parseMultipleFilter(price);
            for (FilterType filter : priceFilter) {
                specifications = specifications.and(filterIntegerField("price", filter.getOperator(), filter.getValue()));
            }
        }

        if (StringUtils.hasText(stock)) {
            List<FilterType> stockFilter = FilteringSortingUtility.parseMultipleFilter(stock);
            for (FilterType filter : stockFilter) {
                specifications = specifications.and(filterLongField("stock", filter.getOperator(), filter.getValue()));
            }
        }

        return specifications;
    }

    private static Specification<Product> hasName(String name) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), name.toLowerCase() + "%");
    }

    private static Specification<Product> hasProductTypeId(Integer productTypeId) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("productType").get("id"), productTypeId);
    }

    public static Specification<Product> filterIntegerField(String field, String operator, String value) {
        return (root, query, criteriaBuilder) ->
                switch (operator) {
                    case "e" -> criteriaBuilder.equal(root.get(field), Integer.parseInt(value));
                    case "gt" -> criteriaBuilder.greaterThan(root.get(field), Integer.parseInt(value));
                    case "gte" -> criteriaBuilder.greaterThanOrEqualTo(root.get(field), Integer.parseInt(value));
                    case "lt" -> criteriaBuilder.lessThan(root.get(field), Integer.parseInt(value));
                    case "lte" -> criteriaBuilder.lessThanOrEqualTo(root.get(field), Integer.parseInt(value));
                    default -> throw new ResponseStatusException(HttpStatus.BAD_REQUEST, MessageConstants.INVALID_OPERATOR_TYPE);
                };
    }

    public static Specification<Product> filterLongField(String field, String operator, String value) {
        return (root, query, criteriaBuilder) ->
                switch (operator) {
                    case "e" -> criteriaBuilder.equal(root.get(field), Long.parseLong(value));
                    case "gt" -> criteriaBuilder.greaterThan(root.get(field), Long.parseLong(value));
                    case "gte" -> criteriaBuilder.greaterThanOrEqualTo(root.get(field), Long.parseLong(value));
                    case "lt" -> criteriaBuilder.lessThan(root.get(field), Long.parseLong(value));
                    case "lte" -> criteriaBuilder.lessThanOrEqualTo(root.get(field), Long.parseLong(value));
                    default -> throw new ResponseStatusException(HttpStatus.BAD_REQUEST, MessageConstants.INVALID_OPERATOR_TYPE);
                };
    }
}
