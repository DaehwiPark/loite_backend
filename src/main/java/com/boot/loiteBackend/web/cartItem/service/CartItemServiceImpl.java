package com.boot.loiteBackend.web.cartItem.service;

import com.boot.loiteBackend.web.cartItem.dto.CartItemRequestDto;
import com.boot.loiteBackend.web.cartItem.dto.CartItemResponseDto;
import com.boot.loiteBackend.web.cartItem.entity.CartItemEntity;
import com.boot.loiteBackend.web.cartItem.projection.CartItemProjection;
import com.boot.loiteBackend.web.cartItem.repository.CartItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CartItemServiceImpl implements CartItemService {

    private final CartItemRepository cartItemRepository;

    @Override
    @Transactional
    public void addToCart(Long userId, CartItemRequestDto requestDto) {
        CartItemEntity existingItem = cartItemRepository
                .findByUserIdAndProductIdAndOptionText(userId, requestDto.getProductId(), requestDto.getOptionText())
                .orElse(null);

        if (existingItem != null) {
            int newQuantity = existingItem.getQuantity() + requestDto.getQuantity();
            existingItem.setQuantity(newQuantity);
            existingItem.setUpdatedAt(LocalDateTime.now());
        } else {
            CartItemEntity newItem = CartItemEntity.builder()
                    .userId(userId)
                    .productId(requestDto.getProductId())
                    .productOptionId(requestDto.getProductOptionId())
                    .optionText(requestDto.getOptionText())
                    .quantity(requestDto.getQuantity())
                    .checkedYn("1")
                    .createdAt(LocalDateTime.now())
                    .build();

            cartItemRepository.save(newItem);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<CartItemResponseDto> getCartItemsByUser(Long userId) {
        LocalDateTime oneMonthAgo = LocalDateTime.now().minusMonths(1);
        List<CartItemProjection> items = cartItemRepository.findCartItemDetailsByUserId(userId, oneMonthAgo);

        return items.stream()
                .map(p -> CartItemResponseDto.builder()
                        .cartItemId(p.getCartItemId())
                        .productId(p.getProductId())
                        .productName(p.getProductName())
                        .brandName(p.getBrandName())
                        .thumbnailUrl(p.getThumbnailUrl())
                        .optionText(p.getOptionText())
                        .quantity(p.getQuantity())
                        .unitPrice(p.getUnitPrice())
                        .totalPrice(p.getUnitPrice() * p.getQuantity())
                        .checked(p.getChecked())
                        .build())
                .toList();
    }

}
