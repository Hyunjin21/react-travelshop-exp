package com.example.travelshop.dto;

import lombok.*;

import java.util.List;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class CartDto {
    private Long id;
    private String userId;
    private List<CartItemDto> items;
}