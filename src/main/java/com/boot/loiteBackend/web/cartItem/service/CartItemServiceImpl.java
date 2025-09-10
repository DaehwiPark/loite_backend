package com.boot.loiteBackend.web.cartItem.service;

import com.boot.loiteBackend.admin.product.gift.repository.AdminGiftRepository;
import com.boot.loiteBackend.admin.product.option.entity.AdminProductOptionEntity;
import com.boot.loiteBackend.admin.product.option.repository.AdminProductOptionRepository;
import com.boot.loiteBackend.global.error.exception.CustomException;
import com.boot.loiteBackend.web.cartItem.dto.*;
import com.boot.loiteBackend.web.cartItem.entity.CartItemAdditionalEntity;
import com.boot.loiteBackend.web.cartItem.entity.CartItemEntity;
import com.boot.loiteBackend.web.cartItem.entity.CartItemGiftEntity;
import com.boot.loiteBackend.web.cartItem.entity.CartItemOptionEntity;
import com.boot.loiteBackend.web.cartItem.error.CartItemErrorCode;
import com.boot.loiteBackend.web.cartItem.projection.*;
import com.boot.loiteBackend.web.cartItem.repository.*;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
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
import java.util.stream.Collector;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CartItemServiceImpl implements CartItemService {
    @PersistenceContext
    private EntityManager entityManager;

    private final CartItemRepository cartItemRepository;
    private final CartItemGiftRepository cartItemGiftRepository;
    private final CartItemOptionRepository cartItemOptionRepository;
    private final AdminProductOptionRepository productOptionRepository;
    private final AdminGiftRepository adminGiftRepository;
    private final CartItemProductGiftRepository cartItemProductGiftRepository;
    private final CartItemAdditionalRepository cartItemAdditionalRepository;

    @Override
    @Transactional
    public CartItemResponseDto addToCart(Long loginUserId, List<CartItemRequestDto> requestList) {
        for (CartItemRequestDto item : requestList) {

            validateCartItem(item);

            List<Long> optionIds = item.getOptions();
            if (optionIds == null || optionIds.isEmpty()) {
                throw new CustomException(CartItemErrorCode.OPTION_NOT_FOUND);
            }

            List<AdminProductOptionEntity> options = productOptionRepository.findAllById(optionIds);
            if (options.size() != optionIds.size()) {
                throw new CustomException(CartItemErrorCode.OPTION_NOT_FOUND);
            }

            for (AdminProductOptionEntity option : options) {
                if ("Y".equals(option.getSoldOutYn())) {
                    throw new CustomException(CartItemErrorCode.SOLD_OUT_OPTION);
                }
            }

            List<CartItemEntity> existingItems = cartItemRepository.findByUserIdAndProductId(loginUserId, item.getProductId());

            CartItemEntity entity = null;

            for (CartItemEntity existing : existingItems) {
                List<Long> existingOptionIds = cartItemOptionRepository.findOptionIdsByCartItemId(existing.getId());

                // 정렬해서 비교 (순서 달라도 같은 조합으로 인정)
                List<Long> sortedExisting = existingOptionIds.stream().sorted().toList();
                List<Long> sortedNew = optionIds.stream().sorted().toList();

                if (sortedExisting.equals(sortedNew)) {
                    entity = existing;
                    break;
                }
            }

            if (entity != null) {

                entity.setQuantity(entity.getQuantity() + item.getQuantity());
                entity.setUpdatedAt(LocalDateTime.now());
                cartItemRepository.save(entity);
            } else {

                CartItemEntity newItem = CartItemEntity.builder()
                        .userId(loginUserId)
                        .productId(item.getProductId())
                        .quantity(item.getQuantity())
                        .checkedYn("1")
                        .createdAt(LocalDateTime.now())
                        .build();

                cartItemRepository.save(newItem);

                // 옵션 매핑 저장
                for (Long optionId : optionIds) {
                    CartItemOptionEntity optionEntity = CartItemOptionEntity.builder()
                            .cartItemId(newItem.getId())
                            .optionId(optionId)
                            .createdAt(LocalDateTime.now())
                            .build();
                    cartItemOptionRepository.save(optionEntity);
                }

                entity = newItem;
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

            // 추가구성품 저장
            if (item.getAdditionals() != null && !item.getAdditionals().isEmpty()) {
                for (CartItemAdditionalDto additionalDto : item.getAdditionals()) {
                    Optional<CartItemAdditionalEntity> existingAdditional =
                            cartItemAdditionalRepository.findByCartItemIdAndAdditionalId(entity.getId(), additionalDto.getProductAdditionalId());

                    if (existingAdditional.isPresent()) {
                        CartItemAdditionalEntity additionalEntity = existingAdditional.get();
                        additionalEntity.setQuantity(additionalEntity.getQuantity() + additionalDto.getQuantity());
                        additionalEntity.setUpdatedAt(LocalDateTime.now());
                    } else {
                        CartItemAdditionalEntity additionalEntity = CartItemAdditionalEntity.builder()
                                .cartItemId(entity.getId())
                                .additionalId(additionalDto.getProductAdditionalId())
                                .quantity(additionalDto.getQuantity())
                                .createdAt(LocalDateTime.now())
                                .build();
                        cartItemAdditionalRepository.save(additionalEntity);
                    }
                }
            }
        }
        return null;
    }

    private void validateCartItem(CartItemRequestDto item) {
        // 상품 수량 체크
        if (item.getQuantity() == null || item.getQuantity() <= 0) {
            throw new CustomException(CartItemErrorCode.INVALID_QUANTITY);
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

    @Override
    @Transactional(readOnly = true)
    public List<CartItemResponseDto> getCartItemsByUser(Long loginUserId) {
        entityManager.clear();
        LocalDateTime oneMonthAgo = LocalDateTime.now().minusMonths(1);
        List<CartItemProjection> items = cartItemRepository.findCartItemsByUserId(loginUserId, oneMonthAgo);

        Map<Long, List<CartItemProjection>> groupedByCartItemId = items.stream()
                .collect(Collectors.groupingBy(CartItemProjection::getCartItemId));

        List<Long> cartItemIds = new ArrayList<>(groupedByCartItemId.keySet());

        List<CartItemOptionProjection> optionProjections = cartItemOptionRepository.findOptionDetailsByCartItemIds(cartItemIds);

        Map<Long, List<CartItemOptionResponseDto>> optionMap = optionProjections.stream()
                .map(p -> CartItemOptionResponseDto.builder()
                        .cartItemId(p.getCartItemId())
                        .optionId(p.getOptionId())
                        .optionStock(p.getOptionStock())
                        .optionType(p.getOptionType())
                        .optionValue(p.getOptionValue())
                        .optionAdditionalPrice(p.getOptionAdditionalPrice())
                        .optionSoldOutYn(Optional.ofNullable(p.getOptionStock()).orElse(0) <= 0)
                        .build())
                .collect(Collectors.groupingBy(CartItemOptionResponseDto::getCartItemId));

        List<CartItemGiftProjection> giftProjections = cartItemGiftRepository.findGiftDetailsByCartItemIds(cartItemIds);

        Map<Long, List<CartItemGiftResponseDto>> giftMap = giftProjections.stream()
                .map(p -> CartItemGiftResponseDto.builder()
                        .cartItemId(p.getCartItemId())
                        .productGiftId(p.getProductGiftId())
                        .giftName(p.getGiftName())
                        .giftImageUrl(p.getGiftImageUrl())
                        .giftStock(p.getGiftStock())
                        .quantity(p.getQuantity())
                        .giftSoldOutYn(Optional.ofNullable(p.getGiftStock()).orElse(0) <= 0)
                        .build())
                .collect(Collectors.groupingBy(CartItemGiftResponseDto::getCartItemId));

        List<CartItemAdditionalProjection> addtionalProjections = cartItemAdditionalRepository.findAdditionalDetailsByCartItemIds(cartItemIds);

        Map<Long, List<CartItemAdditionalResponseDto>> additionalMap = addtionalProjections.stream()
                .map(p -> CartItemAdditionalResponseDto.builder()
                        .cartItemId(p.getCartItemId())
                        .productAdditionalId(p.getProductAdditionalId())
                        .additionalName(p.getAdditionalName())
                        .additionalImageUrl(p.getAdditionalImageUrl())
                        .additionalStock(p.getAdditionalStock())
                        .additionalPrice(p.getAdditionalPrice())
                        .quantity(p.getQuantity())
                        .additionalSoldOutYn(Optional.ofNullable(p.getAdditionalStock()).orElse(0) <= 0)
                        .build())
                .collect(Collectors.groupingBy(CartItemAdditionalResponseDto::getCartItemId));

        return groupedByCartItemId.entrySet().stream()
                .map(entry -> {
                    CartItemProjection p = entry.getValue().get(0);

                    // 수량: null일 경우 기본값 1
                    int quantity = Optional.ofNullable(p.getQuantity()).orElse(1);

                    // 기본 가격: 할인 가격이 있으면 그것을 사용
                    BigDecimal basePrice = Optional.ofNullable(p.getDiscountedPrice())
                            .orElse(p.getUnitPrice());

                    // 옵션 추가 금액 (여러 옵션이라면 optionMap 합산해야 함)
                    BigDecimal optionTotal = optionMap.getOrDefault(p.getCartItemId(), List.of())
                            .stream()
                            .map(o -> Optional.ofNullable(o.getOptionAdditionalPrice()).orElse(BigDecimal.ZERO))
                            .reduce(BigDecimal.ZERO, BigDecimal::add);

                    // 추가구성품 금액
                    BigDecimal additionalTotal = additionalMap.getOrDefault(p.getCartItemId(), List.of())
                            .stream()
                            .map(a -> a.getAdditionalPrice().multiply(BigDecimal.valueOf(a.getQuantity())))
                            .reduce(BigDecimal.ZERO, BigDecimal::add);

                    BigDecimal totalPrice = basePrice
                            .add(optionTotal)   // 옵션 추가금
                            .add(additionalTotal)   // 추가구성품 가격
                            .multiply(BigDecimal.valueOf(quantity));

                    return CartItemResponseDto.builder()
                            .cartItemId(p.getCartItemId())
                            .productId(p.getProductId())
                            .productName(p.getProductName())
                            .brandName(p.getBrandName())
                            .thumbnailUrl(p.getThumbnailUrl())
                            .quantity(quantity)
                            .unitPrice(p.getUnitPrice())
                            .discountedPrice(p.getDiscountedPrice())
                            .discountRate(Optional.ofNullable(p.getDiscountRate()).orElse(0)) // NPE 방지
                            .totalPrice(totalPrice)
                            .checked(p.getChecked() != null && p.getChecked() == 1)
                            .options(optionMap.getOrDefault(p.getCartItemId(), List.of()))
                            .gifts(giftMap.getOrDefault(p.getCartItemId(), List.of()))
                            .additionals(additionalMap.getOrDefault(p.getCartItemId(), List.of()))
                            .build();
                })
                .toList();
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

    @Transactional
    @Override
    public void updateCartItem(Long loginUserId, Long cartItemId, CartItemUpdateRequestDto dto) {

        // 1. 장바구니 항목 조회
        CartItemEntity cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "장바구니 항목이 존재하지 않습니다."));

        if (!cartItem.getUserId().equals(loginUserId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "본인 장바구니만 수정할 수 있습니다.");
        }

        // 상품 수량 검증
        if (dto.getQuantity() == null || dto.getQuantity() <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수량은 1 이상이어야 합니다.");
        }
        cartItem.setQuantity(dto.getQuantity());
        cartItem.setUpdatedAt(LocalDateTime.now());

        // 2. 옵션 업데이트
        cartItemOptionRepository.deleteByCartItemId(cartItemId);
        if (dto.getOptions() != null && !dto.getOptions().isEmpty()) {
            for (CartItemUpdateRequestDto.CartItemOptionUpdateDto optDto : dto.getOptions()) {
                CartItemOptionEntity optionEntity = CartItemOptionEntity.builder()
                        .cartItemId(cartItemId)
                        .optionId(optDto.getProductOptionId())
                        .createdAt(LocalDateTime.now())
                        .build();
                cartItemOptionRepository.save(optionEntity);
            }
        }

        // 3. 사은품 업데이트
        cartItemGiftRepository.deleteByCartItemId(cartItemId);
        if (dto.getGifts() != null && !dto.getGifts().isEmpty()) {
            int totalGiftQty = dto.getGifts().stream()
                    .mapToInt(g -> Optional.ofNullable(g.getQuantity()).orElse(0))
                    .sum();
            if (totalGiftQty > dto.getQuantity()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "사은품 수량이 상품 수량을 초과할 수 없습니다.");
            }

            List<CartItemGiftEntity> giftEntities = dto.getGifts().stream()
                    .map(g -> CartItemGiftEntity.builder()
                            .cartItemId(cartItemId)
                            .giftId(g.getProductGiftId())
                            .quantity(g.getQuantity())
                            .createdAt(LocalDateTime.now())
                            .build()
                    )
                    .toList();
            cartItemGiftRepository.saveAll(giftEntities);
        }

        // 4. 추가구성품 업데이트
        cartItemAdditionalRepository.deleteByCartItemId(cartItemId);
        if (dto.getAdditionals() != null && !dto.getAdditionals().isEmpty()) {
            List<CartItemAdditionalEntity> additionalEntities = dto.getAdditionals().stream()
                    .map(a -> CartItemAdditionalEntity.builder()
                            .cartItemId(cartItemId)
                            .additionalId(a.getProductAdditionalId())
                            .quantity(a.getQuantity())
                            .createdAt(LocalDateTime.now())
                            .build()
                    )
                    .toList();
            cartItemAdditionalRepository.saveAll(additionalEntities);
        }
    }


    /*@Override
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
    }*/

    @Override
    @Transactional(readOnly = true)
    public List<AvailableOptionResponseDto> getAvailableOptions(Long cartItemId) {
        // 1. 해당 cartItemId로 옵션 목록 + 현재 수량 조회
        List<AvailableOptionProjection> projections =
                cartItemOptionRepository.findOptionsForCartItem(cartItemId);

        // 2. Projection → DTO 변환
        return projections.stream()
                .map(p -> AvailableOptionResponseDto.builder()
                        .optionId(p.getOptionId())
                        .optionValue(p.getOptionValue())
                        .colorCode(p.getColorCode())
                        .quantity(p.getQuantity())
                        .optionStock(p.getOptionStock())
                        .optionSoldOutYn(Optional.ofNullable(p.getOptionStock()).orElse(0) <= 0)
                        .build()
                )
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<AvailableGiftResponseDto> getAvailableGifts(Long cartItemId) {
        List<AvailableGiftProjection> projectionList =
                cartItemProductGiftRepository.findAvailableGiftsForReselect(cartItemId);

        return projectionList.stream()
                .map(p -> AvailableGiftResponseDto.builder()
                        .productGiftId(p.getProductGiftId())
                        .giftName(p.getGiftName())
                        .giftImageUrl(p.getGiftImageUrl())
                        .giftStock(p.getGiftStock())
                        .productName(p.getProductName())
                        .optionValue(p.getOptionValue())
                        .optionColorCode(p.getOptionColorCode())
                        .quantity(p.getQuantity())
                        .giftSoldOutYn(Optional.ofNullable(p.getGiftStock()).orElse(0) <= 0)
                        .build()
                )
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
            Integer qty = gift.getQuantity();
            if (qty == null || qty <= 0) {
                continue;
            }
            
            CartItemGiftEntity newGift = CartItemGiftEntity.builder()
                    .cartItemId(cartItemId)
                    .giftId(gift.getProductGiftId())
                    .quantity(gift.getQuantity())
                    .build();
            cartItemGiftRepository.save(newGift);
        }
    }
}
