package com.example.travelshop.domain;

import jakarta.persistence.*;
import lombok.*;

import java.util.*;

@Entity
@Table(name = "cart_items")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class CartItem {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "cart_id")
    private Cart cart;

    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "product_id")
    private TravelProduct product;

    private int quantity;

    /* 옵션 */
    @ElementCollection
    @CollectionTable(name = "cart_item_options", joinColumns = @JoinColumn(name = "cart_item_id"))
    private Set<OptionSelection> options = new LinkedHashSet<>();

    public CartItem(Cart cart, TravelProduct product, int quantity, Set<OptionSelection> opts) {
        this.cart = cart;
        this.product = product;
        this.quantity = quantity;
        if (opts != null) this.options.addAll(opts);
    }

    public boolean hasSameOptions(Set<OptionSelection> other) {
        Set<OptionSelection> a = new LinkedHashSet<>(options);
        Set<OptionSelection> b = new LinkedHashSet<>(other == null ? Collections.emptySet() : other);
        return a.equals(b);
    }
}
