package com.imannuel.mobile_place_order_system.entity;

import com.imannuel.mobile_place_order_system.constant.DatabaseConstants;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = DatabaseConstants.PRODUCT_TYPE_TABLE)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String name;
}
