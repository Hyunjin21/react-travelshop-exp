package com.example.travelshop.repository;

import com.example.travelshop.domain.Cart;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.*;

public interface CartRepository extends JpaRepository<Cart, Long> {
    Optional<Cart> findByUserId(String userId);
    List<Cart> findAllByUserId(String userId);
    Optional<Cart> findTopByUserIdOrderByIdDesc(String userId);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("delete from Cart c where c.id = :id")
    void deleteByIdBulk(@Param("id") Long id);
}

