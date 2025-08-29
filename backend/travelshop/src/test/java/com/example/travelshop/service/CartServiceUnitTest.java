package com.example.travelshop.service;

import com.example.travelshop.domain.Cart;
import com.example.travelshop.domain.TravelProduct;
import com.example.travelshop.exception.BadRequestException;
import com.example.travelshop.exception.NotFoundException;
import com.example.travelshop.repository.CartItemRepository;
import com.example.travelshop.repository.CartRepository;
import com.example.travelshop.repository.OrderRepository;
import com.example.travelshop.repository.TravelProductRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
class CartServiceUnitTest {

    @Mock CartRepository cartRepo;
    @Mock CartItemRepository cartItemRepo;
    @Mock TravelProductRepository prodRepo;
    @Mock OrderRepository orderRepo;
    @Mock OptionService optionService;

    @InjectMocks CartService svc;

    @Test
    void addToCart_zeroQty_shouldThrow() {
        assertThatThrownBy(() -> svc.addToCart("u1", 1L, 0))
                .isInstanceOf(BadRequestException.class);
    }

    @Test
    void addToCart_missingProduct_shouldThrow() {
        given(cartRepo.findTopByUserIdOrderByIdDesc("u1")).willReturn(Optional.of(new Cart("u1")));
        given(prodRepo.findById(1L)).willReturn(Optional.empty());

        assertThatThrownBy(() -> svc.addToCart("u1", 1L, 1))
                .isInstanceOf(NotFoundException.class);
    }

    @Test
    void addToCart_happyPath() {
        given(cartRepo.findTopByUserIdOrderByIdDesc("u1")).willReturn(Optional.empty());
        given(cartRepo.save(any(Cart.class))).willAnswer(inv -> inv.getArgument(0));
        given(prodRepo.findById(1L)).willReturn(Optional.of(
                TravelProduct.builder().id(1L).title("T").price(100).build()
        ));
        given(optionService.toSelections(any())).willReturn(java.util.Set.of());

        var dto = svc.addToCart("u1", 1L, 2, java.util.List.of("Insurance"));
        assertThat(dto.getUserId()).isEqualTo("u1");
        assertThat(dto.getItems()).hasSize(1);
    }
}
