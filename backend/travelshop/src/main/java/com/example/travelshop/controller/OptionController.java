package com.example.travelshop.controller;

import com.example.travelshop.dto.OptionDto;
import com.example.travelshop.service.OptionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/options")
@Tag(name="OptionItem", description="여행 옵션 조회 API")
@RequiredArgsConstructor
public class OptionController {

    private final OptionService svc;

    @Operation(summary="전체 옵션 조회 또는 키워드 검색")
    @GetMapping
    public List<OptionDto> list(@RequestParam(name="q", required=false) String q) {
        return svc.findAll(Optional.ofNullable(q));
    }

    @Operation(summary="옵션 상세 조회")
    @GetMapping("/{id}")
    public OptionDto get(@PathVariable Long id) {
        return svc.getById(id);
    }
}
