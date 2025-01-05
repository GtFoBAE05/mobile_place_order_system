package com.imannuel.mobile_place_order_system.entity;

import com.imannuel.mobile_place_order_system.constant.DatabaseConstants;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = DatabaseConstants.PRODUCT_TABLE)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(nullable = false)
    private String name;

    @ManyToOne
    @JoinColumn(name = "product_type_id", nullable = false)
    private ProductType productType;

    @Column(nullable = false)
    private Integer stock;

    @Column(nullable = false)
    private Long price;
}
