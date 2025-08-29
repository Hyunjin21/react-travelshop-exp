package com.example.travelshop.dto;

import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class OptionDto {
    private Long id;           // 고정 ID(1~3) 부여
    private String name;       // "Insurance" | "Dinner" | "FirstClass"
    private String description; 
    private int price;         // 5000 (원)
}
