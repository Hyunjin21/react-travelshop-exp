package com.example.travelshop.dto;

import com.example.travelshop.domain.TravelProduct;
import lombok.*;
import java.time.LocalDate;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class TravelProductDto {
    private Long id;
    private String title;
    private String category;
    private String description;
    private int price;
    private LocalDate availableFrom;
    private LocalDate availableTo;

    private String imagePath;

    public static TravelProductDto from(TravelProduct p) {
        return TravelProductDto.builder()
            .id(p.getId())
            .title(p.getTitle())
            .category(p.getCategory())
            .description(p.getDescription())
            .price(p.getPrice())
            .availableFrom(p.getAvailableFrom())
            .availableTo(p.getAvailableTo())
            .build();
    }
}
