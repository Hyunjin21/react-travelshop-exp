package com.example.travelshop.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "orders")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String userId;

    @Column(nullable = false)
    private LocalDateTime orderedAt;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "order_id")
    private List<OrderItem> items;

    public static Order fromCart(Cart cart) {
        List<OrderItem> orderItems = cart.getItems().stream().map(i -> {
            OrderItem oi = new OrderItem();
            oi.setProduct(i.getProduct());
            oi.setQuantity(i.getQuantity());
            if (i.getOptions() != null) {
                oi.getOptions().addAll(i.getOptions());
            }
            return oi;
        }).toList();

        return Order.builder()
                .userId(cart.getUserId())
                .orderedAt(LocalDateTime.now())
                .items(orderItems)
                .build();
    }
}
