package com.example.travelshop.repository;

import com.example.travelshop.domain.Cart;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.*;

@DataJpaTest
class CartRepositoryTest {

    @Autowired CartRepository cartRepo;

    @Test
    void findAllByUserId_returnsResults() {
        cartRepo.save(new Cart("userA"));
        var list = cartRepo.findAllByUserId("userA");
        assertThat(list).hasSize(1);
    }
}
