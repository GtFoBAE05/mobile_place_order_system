package com.imannuel.mobile_place_order_system.repository.specification;

import com.imannuel.mobile_place_order_system.entity.ProductType;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

public class ProductTypeSpecification {
    public static Specification<ProductType> getSpecification(String name){
        Specification<ProductType> specifications = Specification.where(null);

        if (StringUtils.hasText(name)) {
            specifications = specifications.and(ProductTypeSpecification.hasName(name));
        }

        return specifications;
    }

    private static Specification<ProductType> hasName(String name) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), name.toLowerCase() + "%");
    }
}
