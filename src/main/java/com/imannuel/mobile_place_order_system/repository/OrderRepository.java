package com.imannuel.mobile_place_order_system.repository;

import com.imannuel.mobile_place_order_system.entity.Customer;
import com.imannuel.mobile_place_order_system.entity.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Order, String> {
    Page<Order> findAllByCustomer(Customer customer, Pageable pageable);
}
