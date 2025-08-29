package com.example.travelshop.controller;

import com.example.travelshop.dto.CreateTravelProductRequestDto;
import com.example.travelshop.dto.TravelProductDto;
import com.example.travelshop.service.TravelProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/products")
@Tag(name = "TravelProduct", description = "여행 상품 조회 및 검색 API")
@RequiredArgsConstructor
public class TravelProductController {

    private final TravelProductService svc;

    @Operation(summary = "전체 상품 조회 또는 키워드 검색",
        description = "q 파라미터가 있으면 제목/설명에서 검색합니다.")
    @GetMapping
    public List<TravelProductDto> list(@RequestParam(name = "q", required = false) String q) {
        return svc.findAll(Optional.ofNullable(q));
    }

    @Operation(summary = "상품 상세 조회", description = "id로 단건 조회합니다.")
    @GetMapping("/{id}")
    public TravelProductDto get(@PathVariable Long id) {
        return svc.getById(id);
    }

    @Operation(summary = "상품 등록(판매자용)")
    @PostMapping
    public TravelProductDto create(
        @Valid @RequestBody CreateTravelProductRequestDto req
    ) {
        return svc.create(req);
    }
}
