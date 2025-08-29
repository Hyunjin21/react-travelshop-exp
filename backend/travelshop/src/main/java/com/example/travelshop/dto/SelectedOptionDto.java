package com.example.travelshop.dto;

import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class SelectedOptionDto {
    private String name;  // "Insurance" | "Dinner" | "FirstClass"
    private int price;    // 5000
}
