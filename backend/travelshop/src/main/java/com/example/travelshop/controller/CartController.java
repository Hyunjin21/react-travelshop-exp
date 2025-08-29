package com.example.travelshop.controller;

import com.example.travelshop.dto.CartDto;
import com.example.travelshop.dto.OrderDto;
import com.example.travelshop.service.CartService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cart")
@Tag(name = "Cart")
@RequiredArgsConstructor
public class CartController {
    private final CartService svc;

    @Operation(summary = "장바구니에 담기")
    @PostMapping
    public CartDto add(
            @RequestParam String userId,
            @RequestParam Long productId,
            @RequestParam(defaultValue = "1") int qty,
            @RequestParam(name = "options", required = false) java.util.List<String> options
    ) {
        return svc.addToCart(userId, productId, qty, options);
    }

    @Operation(summary = "주문하기")
    @PostMapping("/order")
    public OrderDto order(@RequestParam String userId) {
        return svc.order(userId);
    }
}
