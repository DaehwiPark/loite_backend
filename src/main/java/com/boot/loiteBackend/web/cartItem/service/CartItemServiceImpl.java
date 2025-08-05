package com.boot.loiteBackend.web.cartItem.service;

import com.boot.loiteBackend.admin.product.gift.entity.AdminGiftEntity;
import com.boot.loiteBackend.admin.product.gift.repository.AdminGiftRepository;
import com.boot.loiteBackend.admin.product.option.entity.AdminProductOptionEntity;
import com.boot.loiteBackend.admin.product.option.repository.AdminProductOptionRepository;
import com.boot.loiteBackend.global.error.exception.CustomException;
import com.boot.loiteBackend.web.cartItem.dto.*;
import com.boot.loiteBackend.web.cartItem.dto.*;
import com.boot.loiteBackend.web.cartItem.entity.CartItemEntity;
import com.boot.loiteBackend.web.cartItem.error.CartItemErrorCode;
import com.boot.loiteBackend.web.cartItem.entity.CartItemGiftEntity;
import com.boot.loiteBackend.web.cartItem.projection.CartItemProjection;
import com.boot.loiteBackend.web.cartItem.repository.CartItemGiftRepository;
import com.boot.loiteBackend.web.cartItem.repository.CartItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CartItemServiceImpl implements CartItemService {

    private final CartItemRepository cartItemRepository;
    private final CartItemGiftRepository cartItemGiftRepository;
    private final AdminProductOptionRepository productOptionRepository;
    private final AdminGiftRepository adminGiftRepository;

    @Override
    @Transactional
    public void addToCart(Long loginUserId, CartItemRequestDto requestDto) {
        for (CartItemOptionGiftDto item : requestDto.getItems()) {
            validateCartItem(item);

            AdminProductOptionEntity option = productOptionRepository.findById(item.getProductOptionId())
                    .orElseThrow(() -> new CustomException(CartItemErrorCode.OPTION_NOT_FOUND));

            if ("Y".equals(option.getSoldOutYn())) {
                throw new CustomException(CartItemErrorCode.SOLD_OUT_OPTION);
            }

            Optional<CartItemEntity> existingItem = cartItemRepository
                    .findByUserIdAndProductIdAndProductOptionIdAndGiftId(
                            loginUserId,
                            requestDto.getProductId(),
                            item.getProductOptionId(),
                            item.getGiftId()
                    );

            if (existingItem.isPresent()) {
                CartItemEntity entity = existingItem.get();
                entity.setQuantity(entity.getQuantity() + item.getQuantity());
                entity.setUpdatedAt(LocalDateTime.now());
                cartItemRepository.save(entity);
            } else {
                CartItemEntity newItem = CartItemEntity.builder()
                        .userId(loginUserId)
                        .productId(requestDto.getProductId())
                        .productOptionId(item.getProductOptionId())
                        .giftId(item.getGiftId())
                        .quantity(item.getQuantity())
                        .checkedYn("1")
                        .createdAt(LocalDateTime.now())
                        .build();

                cartItemRepository.save(newItem);
            }
        }
    }

    private void validateCartItem(CartItemOptionGiftDto item) {
        if (item.getQuantity() == null || item.getQuantity() <= 0) {
            throw new CustomException(CartItemErrorCode.INVALID_QUANTITY);
        }

        if (item.getGiftId() == null) {
            throw new CustomException(CartItemErrorCode.GIFT_REQUIRED);
        }
    }

    private void handleCartItem(Long userId, Long productId, Long optionId, Long giftId, int quantity) {
        Optional<CartItemEntity> existingItem = cartItemRepository
                .findByUserIdAndProductIdAndProductOptionIdAndGiftId(userId, productId, optionId, giftId);

        if (existingItem.isPresent()) {
            CartItemEntity item = existingItem.get();
            item.setQuantity(item.getQuantity() + quantity);
            item.setUpdatedAt(LocalDateTime.now());
        } else {
            CartItemEntity newItem = CartItemEntity.builder()
                    .userId(userId)
                    .productId(productId)
                    .productOptionId(optionId)
                    .giftId(giftId)
                    .quantity(quantity)
                    .checkedYn("1")
                    .createdAt(LocalDateTime.now())
                    .build();

            cartItemRepository.save(newItem);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<CartItemResponseDto> getCartItemsByUser(Long loginUserId) {
        LocalDateTime oneMonthAgo = LocalDateTime.now().minusMonths(1);
        List<CartItemProjection> items = cartItemRepository.findCartItemsByUserId(loginUserId, oneMonthAgo);

        return items.stream()
                .map(p -> {
                    // 기본 가격: 할인 가격이 있으면 그것을 사용
                    BigDecimal basePrice = Optional.ofNullable(p.getDiscountedPrice())
                            .orElse(p.getUnitPrice());

                    // 옵션 추가 금액: null일 경우 0으로 처리
                    BigDecimal additionalPrice = Optional.ofNullable(p.getOptionAdditionalPrice())
                            .map(BigDecimal::valueOf)
                            .orElse(BigDecimal.ZERO);

                    // 수량: null일 경우 기본값 1
                    int quantity = Optional.ofNullable(p.getQuantity()).orElse(1);

                    // 최종 가격 계산
                    BigDecimal totalPrice = basePrice.add(additionalPrice).multiply(BigDecimal.valueOf(quantity));

                    return CartItemResponseDto.builder()
                            .cartItemId(p.getCartItemId())
                            .productId(p.getProductId())
                            .productName(p.getProductName())
                            .brandName(p.getBrandName())
                            .thumbnailUrl(p.getThumbnailUrl())
                            .optionType(p.getOptionType())
                            .optionValue(p.getOptionValue())
                            .optionAdditionalPrice(p.getOptionAdditionalPrice())
                            .quantity(quantity)
                            .unitPrice(p.getUnitPrice())
                            .discountedPrice(p.getDiscountedPrice())
                            .discountRate(Optional.ofNullable(p.getDiscountRate()).orElse(0)) // NPE 방지
                            .checked(p.getChecked() != null && p.getChecked() == 1)
                            .giftName(p.getGiftName())
                            .giftImageUrl(p.getGiftImageUrl())
                            .build();
                })
                .toList();
    }

    @Override
    @Transactional
    public void updateCheckedYn(Long loginUserId, Long cartItemId, boolean checked) {
        CartItemEntity item = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new IllegalArgumentException("장바구니 항목이 존재하지 않습니다."));

        if (!item.getUserId().equals(loginUserId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "본인의 장바구니 항목만 수정할 수 있습니다.");
        }

        item.setCheckedYn(checked ? "1" : "0");
    }

    /*@Override
    @Transactional
    public void deleteCartItems(Long userId, List<Long> cartItemIds) {
        List<CartItemEntity> items = cartItemRepository.findAllById(cartItemIds);

        for (CartItemEntity item : items) {
            if (!item.getUserId().equals(userId)) {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "본인의 장바구니 항목만 삭제할 수 있습니다.");
            }
        }

        cartItemRepository.deleteAll(items);
    }*/

    @Override
    @Transactional
    public void deleteCartItems(Long loginUserId, List<Long> cartItemIds) {
        List<CartItemEntity> cartItems = cartItemRepository.findByUserIdAndIdIn(loginUserId, cartItemIds);

        if (cartItems.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "삭제할 장바구니 항목이 없습니다.");
        }

        cartItemRepository.deleteAll(cartItems);
    }


    @Override
    @Transactional
    public void updateCartItemOption(Long loginUserId, Long cartItemId, CartItemOptionUpdateRequestDto requestDto) {
        CartItemEntity cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new IllegalArgumentException("장바구니 항목이 존재하지 않습니다."));

        AdminProductOptionEntity newOption = productOptionRepository.findById(requestDto.getProductOptionId())
                .orElseThrow(() -> new IllegalArgumentException("해당 옵션이 존재하지 않습니다."));

        if ("Y".equals(newOption.getSoldOutYn())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "품절된 옵션으로 변경할 수 없습니다.");
        }

        cartItem.setProductOptionId(newOption.getOptionId());
        cartItem.setUpdatedAt(LocalDateTime.now());
    }

/*
    @Override
    @Transactional
    public void updateCartItemGift(Long loginUserId, Long cartItemId, CartItemGiftUpdateRequestDto requestDto) {
        CartItemEntity cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new CustomException(CartItemErrorCode.CART_ITEM_NOT_FOUND));

        AdminGiftEntity newGift = adminGiftRepository.findById(requestDto.getGiftId())
                .orElseThrow(() -> new CustomException(CartItemErrorCode.GIFT_NOT_FOUND));

        if ("Y".equals(newGift.getSoldOutYn())) {
            throw new CustomException(CartItemErrorCode.SOLD_OUT_GIFT);
        }

        cartItem.setGiftId(newGift.getGiftId());
        cartItem.setUpdatedAt(LocalDateTime.now());
    }
*/

    @Override
    @Transactional
    public void updateCartItemQuantity(Long loginUserId, Long cartItemId, CartItemQuantityUpdateRequestDto requestDto) {
        CartItemEntity cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new IllegalArgumentException("장바구니 항목이 존재하지 않습니다."));

        if(!cartItem.getUserId().equals(loginUserId)){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "해당 장바구니 항목에 대한 권한이 없습니다.");
        }
        if(requestDto.getQuantity() <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수량은 1 이상이어야 합니다.");
        }

        cartItem.setQuantity(requestDto.getQuantity());
        cartItem.setUpdatedAt(LocalDateTime.now());
    }

    @Override
    @Transactional
    public void updateCartItemGifts(Long loginUserId, Long cartItemId, List<CartItemGiftUpdateRequestDto.GiftItem> dtoList) {
        // 1. 장바구니 항목 유효성 검사
        CartItemEntity cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new IllegalArgumentException("장바구니 항목이 존재하지 않습니다."));

        // 2. 전달받은 giftId 리스트 추출
        List<Long> giftIds = dtoList.stream()
                .map(CartItemGiftUpdateRequestDto.GiftItem::getProductGiftId)
                .toList();

        // 3. 한 번에 사은품 엔티티들 조회
        List<AdminGiftEntity> gifts = adminGiftRepository.findAllById(giftIds);

        // 4. Map<giftId, AdminGiftEntity> 으로 매핑
        Map<Long, AdminGiftEntity> giftMap = gifts.stream()
                .collect(Collectors.toMap(AdminGiftEntity::getGiftId, gift -> gift));

        // 5. 기존 사은품들 삭제
        cartItemGiftRepository.deleteByCartItemId(cartItemId);

        // 6. 새로운 사은품 목록 저장
        List<CartItemGiftEntity> newGifts = dtoList.stream()
                .map(giftItem -> {
                    AdminGiftEntity gift = giftMap.get(giftItem.getProductGiftId());
                    if (gift == null) {
                        throw new IllegalArgumentException("유효하지 않은 사은품 ID입니다: " + giftItem.getProductGiftId());
                    }
                    if ("Y".equals(gift.getSoldOutYn())) {
                        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "품절된 사은품은 선택할 수 없습니다.");
                    }
                    return CartItemGiftEntity.builder()
                            .cartItemId(cartItemId)
                            .giftId(gift.getGiftId())
                            .quantity(giftItem.getQuantity())
                            .createdAt(LocalDateTime.now())
                            .build();
                }).toList();

        cartItemGiftRepository.saveAll(newGifts);
    }
}
