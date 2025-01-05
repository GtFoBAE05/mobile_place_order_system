package com.imannuel.mobile_place_order_system.repository;

import com.imannuel.mobile_place_order_system.entity.ProductType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductTypeRepository extends JpaRepository<ProductType, Integer>, JpaSpecificationExecutor<ProductType> {
    Optional<ProductType> findByIdAndDeletedFalse(Integer id);

    boolean existsByDeletedFalseAndNameEqualsIgnoreCase(String name);
}
