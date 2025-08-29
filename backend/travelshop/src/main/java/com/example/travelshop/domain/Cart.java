package com.example.travelshop.domain;

import jakarta.persistence.*;
import lombok.*;

import java.util.*;

@Entity @Table(name = "carts")
@Getter @Setter @NoArgsConstructor
public class Cart {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="user_id", nullable=false)
    private String userId;

    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CartItem> items = new ArrayList<>();

    public Cart(String userId) { this.userId = userId; }

    public void addItem(TravelProduct p, int qty, Set<OptionSelection> opts) {
        for (CartItem i : items) {
            if (i.getProduct().getId().equals(p.getId()) && i.hasSameOptions(opts)) {
                i.setQuantity(i.getQuantity() + qty);
                return;
            }
        }
        items.add(new CartItem(this, p, qty, opts));
    }

    public void addItem(TravelProduct p, int qty) {
        addItem(p, qty, null);
    }

    public void clear() { items.clear(); }
}
