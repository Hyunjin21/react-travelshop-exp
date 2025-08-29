package com.example.travelshop.domain;

import jakarta.persistence.*;
import lombok.*;

@Embeddable
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class OptionSelection {
    @Column(nullable = false, length = 50)
    private String name;   // "Insurance" | "Dinner" | "FirstClass"

    @Column(nullable = false)
    private int price;     // 5000

    // 중복 제거를 위해 이름 기준 equals/hashCode
    @Override public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof OptionSelection that)) return false;
        return name != null && name.equalsIgnoreCase(that.name);
    }
    @Override public int hashCode() { return name == null ? 0 : name.toLowerCase().hashCode(); }
}
