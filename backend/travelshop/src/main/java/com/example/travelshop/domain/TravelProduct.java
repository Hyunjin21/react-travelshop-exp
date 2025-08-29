package com.example.travelshop.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "travel_products")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class TravelProduct {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String category;
    private String description;
    private int price;
    private LocalDate availableFrom;
    private LocalDate availableTo;
}
