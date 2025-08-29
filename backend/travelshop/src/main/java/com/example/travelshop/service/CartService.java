package com.example.travelshop.service;

import com.example.travelshop.domain.Cart;
import com.example.travelshop.domain.CartItem;
import com.example.travelshop.domain.Order;
import com.example.travelshop.domain.TravelProduct;
import com.example.travelshop.dto.*;
import com.example.travelshop.exception.BadRequestException;
import com.example.travelshop.exception.NotFoundException;
import com.example.travelshop.repository.CartItemRepository;
import com.example.travelshop.repository.CartRepository;
import com.example.travelshop.repository.OrderRepository;
import com.example.travelshop.repository.TravelProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CartService {

    private final CartRepository cartRepo;
    private final CartItemRepository cartItemRepo;
    private final TravelProductRepository prodRepo;
    private final OrderRepository orderRepo;
    private final OptionService optionSvc;

    // 장바구니 담기
    @Transactional
    public CartDto addToCart(String userId, Long productId, int qty) {
        return addToCart(userId, productId, qty, null);
    }

    @Transactional
    public CartDto addToCart(String userId, Long productId, int qty, List<String> optionNames) {
        if (qty <= 0) throw new BadRequestException("수량은 1 이상이어야 합니다.");

        Cart cart = findLatestOrCreate(userId); // 최신 1건만 사용

        TravelProduct prod = prodRepo.findById(productId)
                .orElseThrow(() -> new NotFoundException("상품 없음: id=" + productId));

        var selections = optionSvc.toSelections(optionNames); 
        cart.addItem(prod, qty, selections);
        cart = cartRepo.save(cart);

        return toCartDto(cart);
    }

    // 주문
    @Transactional
    public OrderDto order(String userId) {
        var carts = cartRepo.findAllByUserId(userId);
        if (carts.isEmpty()) {
            throw new BadRequestException("장바구니가 비어 있습니다. userId=" + userId);
        }

        Cart primary = carts.get(0);
        if (carts.size() > 1) {
            for (int i = 1; i < carts.size(); i++) {
                Cart extra = carts.get(i);
                for (CartItem it : new ArrayList<>(extra.getItems())) {
                    primary.addItem(it.getProduct(), it.getQuantity(), it.getOptions());
                }
            }
            primary = cartRepo.saveAndFlush(primary);
        }

        if (primary.getItems().isEmpty()) {
            throw new BadRequestException("장바구니가 비어 있습니다. userId=" + userId);
        }

        Order order = Order.fromCart(primary);
        order = orderRepo.saveAndFlush(order);

        OrderDto dto = OrderDto.from(order);

        for (Cart c : carts) {
            Long cid = c.getId();
            cartItemRepo.deleteByCartId(cid); 
        }
        for (Cart c : carts) {
            cartRepo.deleteByIdBulk(c.getId()); 
        }

        return dto;
    }

    @Transactional
    protected Cart findLatestOrCreate(String userId) {
        return cartRepo.findTopByUserIdOrderByIdDesc(userId)
                .orElseGet(() -> cartRepo.save(new Cart(userId)));
    }

    private CartDto toCartDto(Cart cart) {
        return CartDto.builder()
                .id(cart.getId())
                .userId(cart.getUserId())
                .items(cart.getItems().stream()
                        .map(i -> CartItemDto.builder()
                                .productId(i.getProduct().getId())
                                .title(i.getProduct().getTitle())
                                .quantity(i.getQuantity())
                                .price(i.getProduct().getPrice())
                                .options(i.getOptions().stream()
                                        .map(opt -> SelectedOptionDto.builder()
                                                .name(opt.getName())
                                                .price(opt.getPrice())
                                                .build())
                                        .collect(Collectors.toList()))
                                .build())
                        .collect(Collectors.toList()))
                .build();
    }
}


