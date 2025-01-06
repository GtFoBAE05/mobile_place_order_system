package com.imannuel.mobile_place_order_system.entity;

import com.imannuel.mobile_place_order_system.constant.DatabaseConstants;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = DatabaseConstants.ORDER_TABLE)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Order extends Auditable {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @ManyToOne
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    @Column(name = "grand_total", nullable = false)
    private Long grandTotal;

    @OneToMany(mappedBy = "order")
    private List<OrderItem> orderItems;
}
