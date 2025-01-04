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

    private String name;

    @ManyToOne
    @JoinColumn(name = "product_type_id")
    private ProductType productType;

    private Integer stock;

    private Long price;
}
