package com.imannuel.mobile_place_order_system.repository;

import com.imannuel.mobile_place_order_system.entity.ProductType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductTypeRepository extends JpaRepository<ProductType, Integer>, JpaSpecificationExecutor<ProductType> {
    boolean existsByNameEqualsIgnoreCase(String name);
}
