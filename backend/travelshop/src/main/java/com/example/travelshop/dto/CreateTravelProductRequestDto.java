package com.example.travelshop.dto;

import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDate;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class CreateTravelProductRequestDto {
    @NotBlank
    private String title;

    @NotBlank
    private String category;

    private String description;

    @Min(0)
    private int price;
 
   @NotNull
    private LocalDate availableFrom;

    @NotNull
    private LocalDate availableTo;
}
