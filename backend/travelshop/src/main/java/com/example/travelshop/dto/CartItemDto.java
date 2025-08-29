package com.example.travelshop.dto;

import lombok.*;
import java.util.List;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class CartItemDto {
    private Long productId;
    private String title;
    private int quantity;
    private int price; // unitPrice(상품 단가)
    private List<SelectedOptionDto> options; // 장바구니에 선택된 옵션들
}
