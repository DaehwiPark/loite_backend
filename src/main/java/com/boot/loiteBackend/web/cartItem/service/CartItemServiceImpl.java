package com.boot.loiteBackend.web.cartItem.service;

import com.boot.loiteBackend.admin.product.gift.repository.AdminGiftRepository;
import com.boot.loiteBackend.admin.product.option.entity.AdminProductOptionEntity;
import com.boot.loiteBackend.admin.product.option.repository.AdminProductOptionRepository;
import com.boot.loiteBackend.global.error.exception.CustomException;
import com.boot.loiteBackend.web.cartItem.dto.*;
import com.boot.loiteBackend.web.cartItem.entity.CartItemEntity;
import com.boot.loiteBackend.web.cartItem.entity.CartItemGiftEntity;
import com.boot.loiteBackend.web.cartItem.error.CartItemErrorCode;
import com.boot.loiteBackend.web.cartItem.projection.AvailableGiftProjection;
import com.boot.loiteBackend.web.cartItem.projection.AvailableOptionProjection;
import com.boot.loiteBackend.web.cartItem.projection.CartItemGiftProjection;
import com.boot.loiteBackend.web.cartItem.projection.CartItemProjection;
import com.boot.loiteBackend.web.cartItem.repository.CartItemGiftRepository;
import com.boot.loiteBackend.web.cartItem.repository.CartItemOptionRepository;
import com.boot.loiteBackend.web.cartItem.repository.CartItemProductGiftRepository;
import com.boot.loiteBackend.web.cartItem.repository.CartItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CartItemServiceImpl implements CartItemService {

    private final CartItemRepository cartItemRepository;
    private final CartItemGiftRepository cartItemGiftRepository;
    private final CartItemOptionRepository cartItemOptionRepository;
    private final AdminProductOptionRepository productOptionRepository;
    private final AdminGiftRepository adminGiftRepository;
    private final CartItemProductGiftRepository cartItemProductGiftRepository;

    @Transactional
    @Override
    public void addToCart(Long loginUserId, List<CartItemRequestDto> requestList) {
        for (CartItemRequestDto item : requestList) {

            validateCartItem(item); // 기존 검증 메서드 유지

            AdminProductOptionEntity option = productOptionRepository.findById(item.getProductOptionId())
                    .orElseThrow(() -> new CustomException(CartItemErrorCode.OPTION_NOT_FOUND));

            if ("Y".equals(option.getSoldOutYn())) {
                throw new CustomException(CartItemErrorCode.SOLD_OUT_OPTION);
            }

            CartItemEntity entity;

            Optional<CartItemEntity> existingItem = cartItemRepository
                    .findByUserIdAndProductIdAndProductOptionId(
                            loginUserId,
                            item.getProductId(),
                            item.getProductOptionId()
                    );

            if (existingItem.isPresent()) {
                entity = existingItem.get();
                entity.setQuantity(entity.getQuantity() + item.getQuantity());
                entity.setUpdatedAt(LocalDateTime.now());
                cartItemRepository.save(entity);
            } else {
                entity = CartItemEntity.builder()
                        .userId(loginUserId)
                        .productId(item.getProductId())
                        .productOptionId(item.getProductOptionId())
                        .quantity(item.getQuantity())
                        .checkedYn("1")
                        .createdAt(LocalDateTime.now())
                        .build();

                cartItemRepository.save(entity);
            }

            // 사은품 저장
            if (item.getGifts() != null && !item.getGifts().isEmpty()) {
                for (CartItemGiftDto giftDto : item.getGifts()) {
                    Optional<CartItemGiftEntity> existingGift =
                            cartItemGiftRepository.findByCartItemIdAndGiftId(entity.getId(), giftDto.getProductGiftId());

                    if (existingGift.isPresent()) {
                        CartItemGiftEntity giftEntity = existingGift.get();
                        giftEntity.setQuantity(giftEntity.getQuantity() + giftDto.getQuantity());
                        giftEntity.setUpdatedAt(LocalDateTime.now());
                    } else {
                        CartItemGiftEntity giftEntity = CartItemGiftEntity.builder()
                                .cartItemId(entity.getId())
                                .giftId(giftDto.getProductGiftId())
                                .quantity(giftDto.getQuantity())
                                .createdAt(LocalDateTime.now())
                                .build();
                        cartItemGiftRepository.save(giftEntity);
                    }
                }
            }
        }
    }

    private void validateCartItem(CartItemRequestDto item) {
        // 상품 수량 체크
        if (item.getQuantity() == null || item.getQuantity() <= 0) {
            throw new CustomException(CartItemErrorCode.INVALID_QUANTITY);
        }

        // 사은품 리스트 체크
        if (item.getGifts() == null || item.getGifts().isEmpty()) {
            throw new CustomException(CartItemErrorCode.GIFT_REQUIRED);
        }

        // 각 사은품 수량 체크
        int totalGiftQuantity = 0;
        for (CartItemGiftDto gift : item.getGifts()) {
            if (gift.getProductGiftId() == null) {
                throw new CustomException(CartItemErrorCode.GIFT_REQUIRED);
            }
            if (gift.getQuantity() == null || gift.getQuantity() <= 0) {
                throw new CustomException(CartItemErrorCode.INVALID_QUANTITY);
            }
            totalGiftQuantity += gift.getQuantity();
        }
        if (totalGiftQuantity > item.getQuantity()) {
            throw new CustomException(CartItemErrorCode.EXCEED_GIFT_LIMIT);
        }
    }

    private void handleCartItem(Long userId, Long productId, Long optionId, List<CartItemGiftDto> gifts, int quantity) {
        // 장바구니에 동일 상품+옵션이 있는지 확인 (사은품은 따로 비교 안 함)
        Optional<CartItemEntity> existingItem = cartItemRepository
                .findByUserIdAndProductIdAndProductOptionId(userId, productId, optionId);

        CartItemEntity cartItem;
        if (existingItem.isPresent()) {
            cartItem = existingItem.get();
            cartItem.setQuantity(cartItem.getQuantity() + quantity);
            cartItem.setUpdatedAt(LocalDateTime.now());
        } else {
            cartItem = CartItemEntity.builder()
                    .userId(userId)
                    .productId(productId)
                    .productOptionId(optionId)
                    .quantity(quantity)
                    .checkedYn("1")
                    .createdAt(LocalDateTime.now())
                    .build();
            cartItemRepository.save(cartItem);
        }

        // 사은품 매핑 저장
        if (gifts != null && !gifts.isEmpty()) {
            List<CartItemGiftEntity> giftEntities = gifts.stream()
                    .map(g -> CartItemGiftEntity.builder()
                            .cartItemId(cartItem.getId()) // 장바구니 PK 참조
                            .giftId(g.getProductGiftId()) // tb_product_gift의 PK
                            .quantity(g.getQuantity())
                            .createdAt(LocalDateTime.now())
                            .build())
                    .toList();
            cartItemGiftRepository.saveAll(giftEntities);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<CartItemResponseDto> getCartItemsByUser(Long loginUserId) {
        LocalDateTime oneMonthAgo = LocalDateTime.now().minusMonths(1);
        List<CartItemProjection> items = cartItemRepository.findCartItemsByUserId(loginUserId, oneMonthAgo);

        Map<Long, List<CartItemProjection>> groupedByCartItemId = items.stream()
                .collect(Collectors.groupingBy(CartItemProjection::getCartItemId));

        List<Long> cartItemIds = new ArrayList<>(groupedByCartItemId.keySet());

        List<CartItemGiftProjection> giftProjections = cartItemGiftRepository.findGiftDetailsByCartItemIds(cartItemIds);

        Map<Long, List<CartItemGiftResponseDto>> giftMap = giftProjections.stream()
                .map(p -> CartItemGiftResponseDto.builder()
                        .cartItemId(p.getCartItemId())
                        .productGiftId(p.getProductGiftId())
                        .giftName(p.getGiftName())
                        .giftImageUrl(p.getGiftImageUrl())
                        .giftStock(p.getGiftStock())
                        .quantity(p.getQuantity())
                        .build())
                .collect(Collectors.groupingBy(CartItemGiftResponseDto::getCartItemId));

        return groupedByCartItemId.entrySet().stream()
                .map(entry -> {
                    CartItemProjection p = entry.getValue().get(0);

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
                            .optionStock(p.getOptionStock())
                            .optionType(p.getOptionType())
                            .optionValue(p.getOptionValue())
                            .optionAdditionalPrice(p.getOptionAdditionalPrice())
                            .quantity(quantity)
                            .unitPrice(p.getUnitPrice())
                            .discountedPrice(p.getDiscountedPrice())
                            .discountRate(Optional.ofNullable(p.getDiscountRate()).orElse(0)) // NPE 방지
                            .checked(p.getChecked() != null && p.getChecked() == 1)
                            .gifts(giftMap.getOrDefault(p.getCartItemId(), List.of()))
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

    @Override
    @Transactional
    public void deleteCartItems(Long loginUserId, List<Long> cartItemIds) {
        List<CartItemEntity> cartItems = cartItemRepository.findByUserIdAndIdIn(loginUserId, cartItemIds);

        if (cartItems.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "삭제할 장바구니 항목이 없습니다.");
        }

        cartItemRepository.deleteAll(cartItems);
    }


//    @Override
//    @Transactional
//    public void updateCartItemOption(Long loginUserId, Long cartItemId, CartItemOptionUpdateRequestDto requestDto) {
//        CartItemEntity cartItem = cartItemRepository.findById(cartItemId)
//                .orElseThrow(() -> new IllegalArgumentException("장바구니 항목이 존재하지 않습니다."));
//
//        AdminProductOptionEntity newOption = productOptionRepository.findById(requestDto.getProductOptionId())
//                .orElseThrow(() -> new IllegalArgumentException("해당 옵션이 존재하지 않습니다."));
//
//        if ("Y".equals(newOption.getSoldOutYn())) {
//            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "품절된 옵션으로 변경할 수 없습니다.");
//        }
//
//        cartItem.setProductOptionId(newOption.getOptionId());
//        if (requestDto.getQuantity() != null && requestDto.getQuantity() > 0) {
//            cartItem.setQuantity(requestDto.getQuantity());
//
//            List<CartItemGiftEntity> gifts = cartItemGiftRepository.findByCartItemId(cartItemId);
//            int totalGiftQty = gifts.stream().mapToInt(CartItemGiftEntity::getQuantity).sum();
//
//            int newQuantity = requestDto.getQuantity();
//
//            if (totalGiftQty > newQuantity) {
//                int toRemove = totalGiftQty - newQuantity;
//                for (CartItemGiftEntity gift : gifts) {
//                    int q = gift.getQuantity();
//                    if (q <= toRemove) {
//                        toRemove -= q;
//                        cartItemGiftRepository.delete(gift); // 초과된 사은품 삭제
//                    } else {
//                        gift.setQuantity(q - toRemove); // 일부만 줄이기
//                        cartItemGiftRepository.save(gift);
//                        break;
//                    }
//                }
//            }
//        }
//        cartItem.setUpdatedAt(LocalDateTime.now());
//    }

    @Override
    @Transactional
    public void updateCartItemOption(Long loginUserId, Long cartItemId, CartItemOptionUpdateRequestDto requestDto) {

        CartItemEntity cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "장바구니 항목이 존재하지 않습니다."));

        if (!cartItem.getUserId().equals(loginUserId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "본인 장바구니만 수정할 수 있습니다.");
        }

        AdminProductOptionEntity newOption = productOptionRepository.findById(requestDto.getProductOptionId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "해당 옵션이 존재하지 않습니다."));

        if (!newOption.getProduct().getProductId().equals(cartItem.getProductId())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "해당 상품의 옵션이 아닙니다.");
        }

        if ("Y".equalsIgnoreCase(newOption.getSoldOutYn())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "품절된 옵션으로 변경할 수 없습니다.");
        }

        Integer reqQty = requestDto.getQuantity();
        if (reqQty == null || reqQty <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수량은 1 이상이어야 합니다.");
        }
        if (newOption.getOptionStock() < reqQty) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "옵션 재고(" + newOption.getOptionStock() + ")를 초과했습니다.");
        }

        cartItem.setProductOptionId(newOption.getOptionId());

        cartItem.setQuantity(reqQty);

        List<CartItemGiftEntity> gifts = cartItemGiftRepository.findByCartItemId(cartItemId);
        int totalGiftQty = gifts.stream().mapToInt(CartItemGiftEntity::getQuantity).sum();

        if (totalGiftQty > reqQty) {
            int toRemove = totalGiftQty - reqQty;
            for (CartItemGiftEntity gift : gifts) {
                int q = gift.getQuantity();
                if (q <= toRemove) {
                    toRemove -= q;
                    cartItemGiftRepository.delete(gift);
                } else {
                    gift.setQuantity(q - toRemove);
                    cartItemGiftRepository.save(gift);
                    break;
                }
            }
        }

        // 10. 업데이트 시간 변경
        cartItem.setUpdatedAt(LocalDateTime.now());
    }

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
    @Transactional(readOnly = true)
    public List<AvailableOptionResponseDto> getAvailableOptions(Long cartItemId) {
        // 1. 해당 cartItemId로 옵션 목록 + 현재 수량 조회
        List<AvailableOptionProjection> projections =
                cartItemOptionRepository.findOptionsForCartItem(cartItemId);

        // 2. Projection → DTO 변환
        return projections.stream()
                .map(p -> new AvailableOptionResponseDto(
                        p.getOptionId(),
                        p.getOptionValue(),
                        p.getColorCode(),
                        p.getQuantity()
                ))
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<AvailableGiftResponseDto> getAvailableGifts(Long cartItemId) {
        List<AvailableGiftProjection> projectionList =
                cartItemProductGiftRepository.findAvailableGiftsForReselect(cartItemId);

        return projectionList.stream()
                .map(p -> new AvailableGiftResponseDto(
                        p.getProductGiftId(),
                        p.getGiftName(),
                        p.getGiftImageUrl(),
                        p.getGiftStock(),
                        p.getProductName(),
                        p.getOptionValue(),
                        p.getOptionColorCode(),
                        p.getQuantity()
                ))
                .toList();
    }

    @Override
    @Transactional
    public void updateCartItemGifts(Long loginUserId, Long cartItemId, CartItemGiftUpdateRequestDto dto) {
        CartItemEntity cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new IllegalArgumentException("장바구니 항목이 존재하지 않습니다."));

        if (!cartItem.getUserId().equals(loginUserId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "해당 장바구니에 대한 권한이 없습니다.");
        }

        int productQuantity = cartItem.getQuantity(); // 장바구니 상품 수량
        int totalGiftQuantity = dto.getGifts().stream().mapToInt(g -> Optional.ofNullable(g.getQuantity()).orElse(0)).sum();

        if (totalGiftQuantity > productQuantity) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "사은품 수량이 상품 수량을 초과할 수 없습니다.");
        }

        // 기존 사은품 전체 삭제
        cartItemGiftRepository.deleteByCartItemId(cartItemId);

        // 새 사은품 저장
        for (CartItemGiftUpdateRequestDto.CartItemGiftUpdateDto gift : dto.getGifts()) {
            CartItemGiftEntity newGift = CartItemGiftEntity.builder()
                    .cartItemId(cartItemId)
                    .giftId(gift.getProductGiftId())
                    .quantity(gift.getQuantity())
                    .build();
            cartItemGiftRepository.save(newGift);
        }
    }
}
