package com.example.travelshop.controller;

import com.example.travelshop.dto.CartDto;
import com.example.travelshop.service.CartService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CartController.class)
@AutoConfigureMockMvc
class CartControllerTest {

    @Autowired MockMvc mvc;
    @MockBean CartService svc;

    @Test
    void addToCart_withMultipleOptions_returns200() throws Exception {
        given(svc.addToCart(eq("u1"), eq(10L), eq(1), anyList()))
                .willReturn(CartDto.builder().id(1L).userId("u1").items(List.of()).build());

        mvc.perform(post("/api/cart")
                .param("userId", "u1")
                .param("productId", "10")
                .param("qty", "1")
                .param("options", "Insurance")
                .param("options", "Dinner"))
           .andExpect(status().isOk())
           .andExpect(jsonPath("$.id").value(1));
    }
}
