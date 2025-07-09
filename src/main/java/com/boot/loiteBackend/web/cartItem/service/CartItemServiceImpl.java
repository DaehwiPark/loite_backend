package com.boot.loiteBackend.web.cartItem.service;

import com.boot.loiteBackend.admin.product.option.entity.AdminProductOptionEntity;
import com.boot.loiteBackend.admin.product.option.repository.AdminProductOptionRepository;
import com.boot.loiteBackend.web.cartItem.dto.CartItemRequestDto;
import com.boot.loiteBackend.web.cartItem.dto.CartItemResponseDto;
import com.boot.loiteBackend.web.cartItem.entity.CartItemEntity;
import com.boot.loiteBackend.web.cartItem.projection.CartItemProjection;
import com.boot.loiteBackend.web.cartItem.repository.CartItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CartItemServiceImpl implements CartItemService {

    private final CartItemRepository cartItemRepository;
    private final AdminProductOptionRepository productOptionRepository;

    @Override
    @Transactional
    public void addToCart(Long userId, CartItemRequestDto requestDto) {
        AdminProductOptionEntity option = productOptionRepository.findById(requestDto.getProductOptionId())
                .orElseThrow(() -> new IllegalArgumentException("해당 옵션이 존재하지 않습니다."));

        if ("Y".equals(option.getSoldOutYn())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "품절된 옵션은 장바구니에 담을 수 없습니다.");
        }

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
        List<CartItemProjection> items = cartItemRepository.findCartItemsByUserId(userId, oneMonthAgo);

        return items.stream()
                .map(p -> {
                    BigDecimal basePrice = p.getDiscountedPrice() != null
                            ? p.getDiscountedPrice()
                            : p.getUnitPrice();
                    BigDecimal totalPrice = basePrice
                            .add(BigDecimal.valueOf(p.getOptionAdditionalPrice()))
                            .multiply(BigDecimal.valueOf(p.getQuantity()));

                    return CartItemResponseDto.builder()
                            .cartItemId(p.getCartItemId())
                            .productId(p.getProductId())
                            .productName(p.getProductName())
                            .brandName(p.getBrandName())
                            .thumbnailUrl(p.getThumbnailUrl())
                            .optionType(p.getOptionType())
                            .optionValue(p.getOptionValue())
                            .optionAdditionalPrice(p.getOptionAdditionalPrice())
                            .quantity(p.getQuantity())
                            .unitPrice(p.getUnitPrice())
                            .discountedPrice(p.getDiscountedPrice())
                            .discountRate(p.getDiscountRate())
                            .checked(p.getChecked() == 1)
                            .build();
                })
                .toList();
    }

    @Override
    @Transactional
    public void updateCheckedYn(Long userId, Long cartItemId, boolean checked) {
        CartItemEntity item = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new IllegalArgumentException("장바구니 항목이 존재하지 않습니다."));

        if (!item.getUserId().equals(userId)) {
            System.out.println("요청한 유저 ID: " + userId);
            System.out.println("장바구니 항목의 유저 ID: " + item.getUserId());
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "본인의 장바구니 항목만 수정할 수 있습니다.");
        }

        item.setCheckedYn(checked ? "1" : "0");
    }

    @Override
    @Transactional
    public void deleteCartItems(Long userId, List<Long> cartItemIds) {
        List<CartItemEntity> items = cartItemRepository.findAllById(cartItemIds);

        for (CartItemEntity item : items) {
            if (!item.getUserId().equals(userId)) {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "본인의 장바구니 항목만 삭제할 수 있습니다.");
            }
        }

        cartItemRepository.deleteAll(items);
    }

    @Override
    @Transactional
    public void deleteCheckedCartItems(Long userId) {
        List<CartItemEntity> checkedItems = cartItemRepository.findByUserIdAndCheckedYn(userId, "1");

        if (checkedItems.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "삭제할 체크된 항목이 없습니다.");
        }

        cartItemRepository.deleteAll(checkedItems);
    }

}
