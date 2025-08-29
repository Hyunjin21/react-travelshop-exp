package com.example.travelshop.dto;

import com.example.travelshop.domain.Order;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class OrderDto {
    private Long id;
    private String userId;
    private LocalDateTime orderedAt;
    private List<OrderItemDto> items;

    public static OrderDto from(Order o) {
        List<OrderItemDto> itemDtos = o.getItems().stream()
            .map(i -> OrderItemDto.builder()
                    .productId(i.getProduct().getId())
                    .title(i.getProduct().getTitle())
                    .quantity(i.getQuantity())
                    .price(i.getProduct().getPrice())
                    .options(i.getOptions().stream()
                        .map(opt -> SelectedOptionDto.builder()
                                .name(opt.getName())
                                .price(opt.getPrice())
                                .build())
                        .toList())
                    .build())
            .toList();

        return OrderDto.builder()
            .id(o.getId())
            .userId(o.getUserId())
            .orderedAt(o.getOrderedAt())
            .items(itemDtos)
            .build();
    }
}
