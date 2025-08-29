package com.example.travelshop.dto;

import lombok.*;
import java.util.List;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class OrderItemDto {
    private Long productId;
    private String title;
    private int quantity;
    private int price; // unitPrice(상품 단가)
    private List<SelectedOptionDto> options; // 주문 라인의 옵션
}
