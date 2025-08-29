package com.example.travelshop.service;

import com.example.travelshop.domain.Cart;
import com.example.travelshop.domain.TravelProduct;
import com.example.travelshop.repository.CartRepository;
import com.example.travelshop.repository.OrderRepository;
import com.example.travelshop.repository.TravelProductRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.ActiveProfiles;
import org.mockito.ArgumentCaptor;

import java.util.Collections;
import java.util.Optional;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.*;

@SpringBootTest
@ActiveProfiles("test")
class CartServiceTest {

    @MockitoBean private CartRepository cartRepo;
    @MockitoBean private TravelProductRepository prodRepo;
    @MockitoBean private OrderRepository orderRepo;
    @Autowired  private CartService svc;

    @Test
    void addToCart_createsNewCartAndAddsItem() {
        // 장바구니 없음
        given(cartRepo.findAllByUserId("u1")).willReturn(Collections.emptyList());
        given(cartRepo.save(any(Cart.class))).willAnswer(inv -> inv.getArgument(0));

        TravelProduct p = TravelProduct.builder()
                .id(1L).title("T").price(100).build();
        given(prodRepo.findById(1L)).willReturn(Optional.of(p));

        svc.addToCart("u1", 1L, 2);

        ArgumentCaptor<Cart> captor = ArgumentCaptor.forClass(Cart.class);
        then(cartRepo).should(times(2)).save(captor.capture());

        var savedCalls = captor.getAllValues();
        Cart lastSaved = savedCalls.get(savedCalls.size() - 1);

        assertThat(lastSaved.getUserId()).isEqualTo("u1");
        assertThat(lastSaved.getItems()).hasSize(1);
    }
}
