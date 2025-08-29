package com.example.travelshop.service;

import com.example.travelshop.domain.OptionSelection;
import com.example.travelshop.dto.OptionDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OptionService {

    private static final int PRICE_WON = 5000;

    private static final List<OptionDto> OPTIONS = List.of(
        OptionDto.builder().id(1L).name("Insurance").  description("안전한 여행을 위해서!").          price(PRICE_WON).build(),
        OptionDto.builder().id(2L).name("Dinner").     description("맛있는 저녁과 함께하는 여행!").  price(PRICE_WON).build(),
        OptionDto.builder().id(3L).name("FirstClass"). description("편안한 비행을 위해서!").        price(PRICE_WON).build()
    );

    public List<OptionDto> findAll(Optional<String> q) {
        return q.filter(s -> !s.isBlank())
                .map(keyword -> {
                    String k = keyword.toLowerCase(Locale.ROOT);
                    return OPTIONS.stream()
                            .filter(o -> o.getName().toLowerCase(Locale.ROOT).contains(k)
                                   || (o.getDescription()!=null && o.getDescription().toLowerCase(Locale.ROOT).contains(k)))
                            .toList();
                })
                .orElse(OPTIONS);
    }

    public OptionDto getById(Long id) {
        return OPTIONS.stream()
                .filter(o -> o.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Option not found: " + id));
    }


    public boolean isValidName(String name) {
        return OPTIONS.stream().anyMatch(o -> o.getName().equalsIgnoreCase(name));
    }

    /* 이름 리스트 → Set */
    public Set<OptionSelection> toSelections(List<String> names) {
        if (names == null || names.isEmpty()) return new LinkedHashSet<>();
        Map<String, OptionDto> byLower = OPTIONS.stream()
                .collect(Collectors.toMap(o -> o.getName().toLowerCase(Locale.ROOT), o -> o));
        LinkedHashSet<OptionSelection> out = new LinkedHashSet<>();
        for (String raw : names) {
            if (raw == null) continue;
            String key = raw.trim().toLowerCase(Locale.ROOT);
            if (key.isEmpty()) continue;
            OptionDto opt = byLower.get(key);
            if (opt != null) {
                out.add(new OptionSelection(opt.getName(), opt.getPrice())); 
            }
        }
        return out;
    }
}
