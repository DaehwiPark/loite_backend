package com.boot.loiteBackend.web.cartItem.service;

import com.boot.loiteBackend.admin.product.gift.entity.AdminGiftEntity;
import com.boot.loiteBackend.admin.product.gift.repository.AdminGiftRepository;
import com.boot.loiteBackend.admin.product.option.entity.AdminProductOptionEntity;
import com.boot.loiteBackend.admin.product.option.repository.AdminProductOptionRepository;
import com.boot.loiteBackend.web.cartItem.dto.*;
import com.boot.loiteBackend.web.cartItem.entity.CartItemEntity;
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
        AdminProductOptionEntity option = productOptionRepository.findById(requestDto.getProductOptionId())
                .orElseThrow(() -> new IllegalArgumentException("해당 옵션이 존재하지 않습니다."));

        if ("Y".equals(option.getSoldOutYn())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "품절된 옵션은 장바구니에 담을 수 없습니다.");
        }

        Optional<CartItemEntity> existingItem = cartItemRepository
                .findByUserIdAndProductIdAndProductOptionId(
                        loginUserId,
                        requestDto.getProductId(),
                        requestDto.getProductOptionId()
                );


        if (existingItem.isPresent()) {
            CartItemEntity item = existingItem.get();
            int newQuantity = item.getQuantity() + requestDto.getQuantity();
            item.setQuantity(newQuantity);
            item.setUpdatedAt(LocalDateTime.now());
        } else {
            CartItemEntity newItem = CartItemEntity.builder()
                    .userId(loginUserId)
                    .productId(requestDto.getProductId())
                    .productOptionId(requestDto.getProductOptionId())
                    .quantity(requestDto.getQuantity())
                    .checkedYn("1")
                    .createdAt(LocalDateTime.now())
                    .build();

            cartItemRepository.save(newItem);

            if (requestDto.getGiftIdList() != null && !requestDto.getGiftIdList().isEmpty()) {
                List<CartItemGiftEntity> giftEntities = requestDto.getGiftIdList().stream()
                        .map(giftId -> CartItemGiftEntity.builder()
                                .cartItemId(newItem.getId())
                                .giftId(giftId)
                                .quantity(1)  // 기본 1개. 수량 조정 필요하면 여기에 로직 추가
                                .createdAt(LocalDateTime.now())
                                .build())
                        .collect(Collectors.toList());

                cartItemGiftRepository.saveAll(giftEntities);
            }
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<CartItemResponseDto> getCartItemsByUser(Long loginUserId) {
        LocalDateTime oneMonthAgo = LocalDateTime.now().minusMonths(1);
        List<CartItemProjection> items = cartItemRepository.findCartItemsByUserId(loginUserId, oneMonthAgo);

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
                .orElseThrow(() -> new IllegalArgumentException("장바구니 항목이 존재하지 않습니다."));

        AdminGiftEntity newGift = adminGiftRepository.findById(requestDto.getGiftId())
                .orElseThrow(() -> new IllegalArgumentException("해당 사은품이 존재하지 않습니다."));

        if ("Y".equals(newGift.getSoldOutYn())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "품절된 사은품으로 변경할 수 없습니다.");
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
                .map(CartItemGiftUpdateRequestDto.GiftItem::getGiftId)
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
                    AdminGiftEntity gift = giftMap.get(giftItem.getGiftId());
                    if (gift == null) {
                        throw new IllegalArgumentException("유효하지 않은 사은품 ID입니다: " + giftItem.getGiftId());
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
