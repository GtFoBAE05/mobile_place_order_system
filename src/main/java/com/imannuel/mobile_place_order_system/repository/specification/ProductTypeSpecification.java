package com.imannuel.mobile_place_order_system.repository.specification;

import com.imannuel.mobile_place_order_system.entity.Product;
import com.imannuel.mobile_place_order_system.entity.ProductType;
import jakarta.persistence.criteria.Join;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

public class ProductTypeSpecification {
    public static Specification<ProductType> getSpecification(String name) {
        Specification<ProductType> specifications = Specification.where(null);
        specifications = specifications.and(ProductTypeSpecification.hasDeletedFalse());


        if (StringUtils.hasText(name)) {
            specifications = specifications.and(ProductTypeSpecification.hasName(name));
        }

        return specifications;
    }

    private static Specification<ProductType> hasName(String name) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), name.toLowerCase() + "%");
    }

    private static Specification<ProductType> hasDeletedFalse() {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.isFalse(root.get("deleted"));
    }

    public static Specification<ProductType> hasProduct(Integer id) {
        return (root, query, criteriaBuilder) -> {
            Join<ProductType, Product> products = root.join("productList");
            return criteriaBuilder.equal(products.get("productType").get("id"), id);
        };
    }
}
