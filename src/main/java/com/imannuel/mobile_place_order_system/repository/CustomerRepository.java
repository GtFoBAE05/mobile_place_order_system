package com.imannuel.mobile_place_order_system.repository;

import com.imannuel.mobile_place_order_system.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, String> {
}
