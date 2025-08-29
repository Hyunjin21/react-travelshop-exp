package com.example.travelshop.it;

import com.example.travelshop.domain.TravelProduct;
import com.example.travelshop.dto.OrderDto;
import com.example.travelshop.repository.TravelProductRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class OrderFlowIT {

    @Autowired TestRestTemplate rest;
    @Autowired TravelProductRepository prodRepo;

    @Test
    void addToCart_thenOrder_success() {
        var p = prodRepo.save(TravelProduct.builder()
                .title("America")
                .category("TRAVEL")
                .description("Good")
                .price(900000)
                .build());

        ResponseEntity<Void> add = rest.postForEntity(
                "/api/cart?userId=u1&productId={pid}&qty=1&options=Insurance",
                null, Void.class, p.getId());
        assertThat(add.getStatusCode().is2xxSuccessful()).isTrue();

        ResponseEntity<OrderDto> order = rest.postForEntity(
                "/api/cart/order?userId=u1", null, OrderDto.class);
        assertThat(order.getStatusCode().is2xxSuccessful()).isTrue();
        assertThat(order.getBody().getId()).isNotNull();
    }
}
