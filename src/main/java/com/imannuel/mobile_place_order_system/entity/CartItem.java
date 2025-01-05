package com.imannuel.mobile_place_order_system.entity;

import com.imannuel.mobile_place_order_system.constant.DatabaseConstants;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = DatabaseConstants.CART_ITEM_TABLE)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CartItem {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @ManyToOne
    @JoinColumn(name = "cart_id", nullable = false)
    private Cart cart;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(nullable = false)
    private Integer quantity;
}
