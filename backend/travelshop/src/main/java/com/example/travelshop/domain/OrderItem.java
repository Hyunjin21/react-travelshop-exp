package com.example.travelshop.domain;

import jakarta.persistence.*;
import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
@Entity @Table(name = "order_items")
public class OrderItem {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "product_id")
    private TravelProduct product;

    private int quantity;

    // 주문 아이템에도 옵션 저장
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "order_item_options", joinColumns = @JoinColumn(name = "order_item_id"))
    private java.util.Set<OptionSelection> options = new java.util.HashSet<>();
}
