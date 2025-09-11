package com.boot.loiteBackend.web.order.service;

import com.boot.loiteBackend.admin.product.additional.entity.AdminAdditionalEntity;
import com.boot.loiteBackend.admin.product.additional.entity.AdminProductAdditionalEntity;
import com.boot.loiteBackend.admin.product.additional.repository.AdminProductAdditionalRepository;
import com.boot.loiteBackend.admin.product.gift.entity.AdminProductGiftEntity;
import com.boot.loiteBackend.admin.product.gift.repository.AdminProductGiftRepository;
import com.boot.loiteBackend.admin.product.option.entity.AdminProductOptionEntity;
import com.boot.loiteBackend.admin.product.option.repository.AdminProductOptionRepository;
import com.boot.loiteBackend.admin.product.product.entity.AdminProductEntity;
import com.boot.loiteBackend.admin.product.product.entity.AdminProductImageEntity;
import com.boot.loiteBackend.admin.product.product.repository.AdminProductRepository;
import com.boot.loiteBackend.domain.user.address.entity.UserAddressEntity;
import com.boot.loiteBackend.web.order.dto.*;
import com.boot.loiteBackend.web.order.entity.*;
import com.boot.loiteBackend.web.order.enums.OrderStatus;
import com.boot.loiteBackend.web.delivery.enums.DeliveryStatus;
import com.boot.loiteBackend.web.order.mapper.OrderMapper;
import com.boot.loiteBackend.web.order.repository.OrderItemRepository;
import com.boot.loiteBackend.web.order.repository.OrderRepository;
import com.boot.loiteBackend.web.order.repository.OrderSequenceRepository;
import com.boot.loiteBackend.web.user.address.repository.UserAddressRepository;
import com.boot.loiteBackend.web.user.address.service.UserAddressService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final AdminProductRepository productRepository;
    private final AdminProductOptionRepository optionRepository;
    private final AdminProductAdditionalRepository additionalRepository;
    private final OrderSequenceRepository orderSequenceRepository;
    private final UserAddressRepository userAddressRepository;
    private final UserAddressService userAddressService;
    private final AdminProductAdditionalRepository productAdditionalRepository;
    private final AdminProductGiftRepository productGiftRepository;
    private final OrderMapper orderMapper;

    @Override
    @Transactional
    public OrderResponseDto createOrder(Long userId, OrderRequestDto requestDto) {

        // 0. 신규 주소 처리
        if (requestDto.getAddressId() == null) {
            if ("Y".equals(requestDto.getDefaultYn())) {
                userAddressRepository.resetDefaultForUser(userId);
            }

            UserAddressEntity userAddressData = UserAddressEntity.builder()
                    .userId(userId)
                    .alias(requestDto.getAlias())
                    .receiverName(requestDto.getReceiverName())
                    .receiverPhone(requestDto.getReceiverPhone())
                    .zipCode(requestDto.getZipCode())
                    .addressLine1(requestDto.getAddressLine1())
                    .addressLine2(requestDto.getAddressLine2())
                    .defaultYn(requestDto.getDefaultYn())
                    .deleteYn("N")
                    .build();

            userAddressRepository.save(userAddressData);
        }

        // 1. 주문번호 생성
        String date = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        orderSequenceRepository.insertSeq(date);
        Long seq = orderSequenceRepository.getLastSeq();
        String orderNumber = "ORD-" + date + "-" + String.format("%06d", seq);

        // 2. 주문 엔티티 생성
        OrderEntity order = OrderEntity.builder()
                .userId(userId)
                .orderNumber(orderNumber)
                .orderStatus(OrderStatus.CREATED)
                .originalAmount(BigDecimal.ZERO)
                .discountAmount(BigDecimal.ZERO)
                .deliveryFee(BigDecimal.ZERO)
                .totalAmount(BigDecimal.ZERO)
                .receiverName(requestDto.getReceiverName())
                .receiverPhone(requestDto.getReceiverPhone())
                .receiverAddress(requestDto.getReceiverAddress())
                .receiverMemo(requestDto.getReceiverMemo())
                .deliveryStatus(DeliveryStatus.READY)
                .createdAt(LocalDateTime.now())
                .build();
        orderRepository.save(order);

        // 3. 금액 계산 변수
        BigDecimal originalTotal = BigDecimal.ZERO;
        BigDecimal discountedTotal = BigDecimal.ZERO;

        // 4. 주문 아이템 처리
        List<OrderItemEntity> orderItems = new ArrayList<>();

        for (OrderItemRequestDto itemDto : requestDto.getOrderItems()) {
            AdminProductEntity product = productRepository.findById(itemDto.getProductId())
                    .orElseThrow(() -> new IllegalArgumentException("상품을 찾을 수 없습니다."));

            BigDecimal basePrice = product.getProductPrice();
            BigDecimal discountedPrice = product.getDiscountedPrice();

            // 본품 기준 아이템 생성
            OrderItemEntity orderItem = OrderItemEntity.builder()
                    .order(order)
                    .product(product)
                    .quantity(itemDto.getQuantity())
                    .unitPrice(basePrice)
                    .discountedPrice(discountedPrice)
                    .createdAt(LocalDateTime.now())
                    .build();

            // === 옵션 처리 ===
            if (itemDto.getOptions() != null) {
                for (OrderOptionRequestDto optDto : itemDto.getOptions()) {
                    AdminProductOptionEntity option = optionRepository.findById(optDto.getOptionId())
                            .orElseThrow(() -> new IllegalArgumentException("옵션을 찾을 수 없습니다."));

                    BigDecimal addPrice = BigDecimal.valueOf(option.getOptionAdditionalPrice());

                    OrderItemOptionEntity optionEntity = OrderItemOptionEntity.builder()
                            .orderItem(orderItem)
                            .productOption(option)
                            .additionalPrice(addPrice)
                            .quantity(optDto.getQuantity())
                            .build();

                    orderItem.getOptions().add(optionEntity);

                    // 옵션 추가금 반영
                    basePrice = basePrice.add(addPrice.multiply(BigDecimal.valueOf(optDto.getQuantity())));
                    discountedPrice = discountedPrice.add(addPrice.multiply(BigDecimal.valueOf(optDto.getQuantity())));
                }
            }

            // === 추가구성품 처리 ===
            if (itemDto.getAdditionals() != null) {
                for (OrderAdditionalRequestDto addDto : itemDto.getAdditionals()) {
                    // 상품-추가구성품 매핑 조회
                    AdminProductAdditionalEntity productAdditional = productAdditionalRepository.findById(addDto.getAdditionalId())
                            .orElseThrow(() -> new IllegalArgumentException("추가구성품 매핑을 찾을 수 없습니다."));

                    // 연결된 마스터 엔티티에서 실제 정보 가져오기
                    AdminAdditionalEntity additional = productAdditional.getAdditional();
                    BigDecimal addPrice = additional.getAdditionalPrice();

                    // 주문 상세 추가구성품 엔티티 생성
                    OrderItemAdditionalEntity additionalEntity = OrderItemAdditionalEntity.builder()
                            .orderItem(orderItem)
                            .productAdditional(productAdditional) // 매핑 엔티티 저장
                            .additionalPrice(addPrice)
                            .quantity(addDto.getQuantity())
                            .build();

                    orderItem.getAdditionals().add(additionalEntity);

                    // 금액 반영
                    basePrice = basePrice.add(addPrice.multiply(BigDecimal.valueOf(addDto.getQuantity())));
                    discountedPrice = discountedPrice.add(addPrice.multiply(BigDecimal.valueOf(addDto.getQuantity())));
                }
            }


            // === 사은품 처리 ===
            if (itemDto.getGifts() != null) {
                for (OrderGiftRequestDto giftDto : itemDto.getGifts()) {
                    AdminProductGiftEntity gift = productGiftRepository.findById(giftDto.getGiftId())
                            .orElseThrow(() -> new IllegalArgumentException("사은품을 찾을 수 없습니다."));

                    OrderItemGiftEntity giftEntity = OrderItemGiftEntity.builder()
                            .orderItem(orderItem)
                            .productGift(gift)
                            .quantity(giftDto.getQuantity())
                            .build();

                    orderItem.getGifts().add(giftEntity);
                }
            }

            // === 총액 계산 ===
            BigDecimal itemOriginalTotal = basePrice.multiply(BigDecimal.valueOf(itemDto.getQuantity()));
            BigDecimal itemDiscountedTotal = discountedPrice.multiply(BigDecimal.valueOf(itemDto.getQuantity()));

            orderItem.setTotalPrice(itemDiscountedTotal);

            order.getOrderItems().add(orderItem);

            originalTotal = originalTotal.add(itemOriginalTotal);
            discountedTotal = discountedTotal.add(itemDiscountedTotal);
        }

        orderItemRepository.saveAll(orderItems);

        // 5. 배송비 계산
        BigDecimal deliveryFee = discountedTotal.compareTo(new BigDecimal("50000")) < 0
                ? new BigDecimal("3000")
                : BigDecimal.ZERO;

        // 6. 주문 총액 갱신
        BigDecimal discountAmount = originalTotal.subtract(discountedTotal);
        BigDecimal finalPayAmount = discountedTotal.add(deliveryFee);

        order.setOriginalAmount(originalTotal);
        order.setDiscountAmount(discountAmount);
        order.setTotalAmount(finalPayAmount);
        order.setDeliveryFee(deliveryFee);
        order.setUpdatedAt(LocalDateTime.now());

        orderRepository.save(order);

        // 7. DTO 변환
        return orderMapper.toDto(order);
    }


    @Override
    @Transactional(readOnly = true)
    public OrderResponseDto getOrder(Long userId, Long orderId) {
        // 주문 조회
        OrderEntity order = orderRepository.findByOrderIdAndUserId(orderId, userId)
                .orElseThrow(() -> new IllegalArgumentException("주문을 찾을 수 없습니다."));

        // 주문 아이템 조회
        List<OrderItemEntity> items = orderItemRepository.findByOrder(order);

        List<OrderItemResponseDto> responseItems = items.stream()
                .map(item -> {
                    String productImageUrl = item.getProduct().getProductImages().stream()
                            .sorted(Comparator.comparingInt(img -> Optional.ofNullable(img.getImageSortOrder()).orElse(9999)))
                            .map(AdminProductImageEntity::getImageUrl)
                            .findFirst()
                            .orElse(null);

                    List<OrderOptionResponseDto> optionDtos = item.getOptions().stream()
                            .map(opt -> OrderOptionResponseDto.builder()
                                    .optionId(opt.getProductOption().getOptionId())
                                    .optionValue(opt.getProductOption().getOptionValue())
                                    .additionalPrice(opt.getAdditionalPrice())
                                    .quantity(opt.getQuantity())
                                    .build())
                            .toList();

                    List<OrderAdditionalResponseDto> additionalDtos = item.getAdditionals().stream()
                            .map(add -> OrderAdditionalResponseDto.builder()
                                    .additionalId(add.getProductAdditional().getProductAdditionalId())
                                    .additionalName(add.getProductAdditional().getAdditional().getAdditionalName())
                                    .additionalPrice(add.getAdditionalPrice())
                                    .quantity(add.getQuantity())
                                    .additionalImageUrl(add.getProductAdditional().getAdditional().getAdditionalImageUrl())
                                    .build())
                            .toList();

                    List<OrderGiftResponseDto> giftDtos = item.getGifts().stream()
                            .map(gift -> OrderGiftResponseDto.builder()
                                    .giftId(gift.getProductGift().getProductGiftId())
                                    .giftName(gift.getProductGift().getGift().getGiftName())
                                    .quantity(gift.getQuantity())
                                    .giftImageUrl(gift.getProductGift().getGift().getGiftImageUrl())
                                    .build())
                            .toList();

                    return OrderItemResponseDto.builder()
                            .productId(item.getProduct().getProductId())
                            .productName(item.getProduct().getProductName())
                            .productImageUrl(productImageUrl)
                            .quantity(item.getQuantity())
                            .unitPrice(item.getUnitPrice())
                            .totalPrice(item.getTotalPrice())
                            .options(optionDtos)
                            .additionals(additionalDtos)
                            .gifts(giftDtos)
                            .build();
                })
                .toList();

        return OrderResponseDto.builder()
                .orderId(order.getOrderId())
                .orderNumber(order.getOrderNumber())
                .orderStatus(order.getOrderStatus().name())
                .originalAmount(order.getOriginalAmount())
                .discountAmount(order.getDiscountAmount())
                .totalAmount(order.getTotalAmount())
                .deliveryFee(order.getDeliveryFee())
                .payAmount(order.getTotalAmount()) // == totalAmount
                .receiverName(order.getReceiverName())
                .receiverPhone(order.getReceiverPhone())
                .receiverAddress(order.getReceiverAddress())
                .receiverMemo(order.getReceiverMemo())
                .createdAt(order.getCreatedAt())
                .items(responseItems)
                .build();
    }


    @Override
    @Transactional(readOnly = true)
    public Page<OrderResponseDto> getOrders(Long userId, Pageable pageable) {
        Page<OrderEntity> orders = orderRepository.findByUserId(userId, pageable);

        return orders.map(order -> {
            List<OrderItemEntity> items = orderItemRepository.findByOrder(order);

            List<OrderItemResponseDto> responseItems = items.stream()
                    .map(item -> {
                        // 상품 대표 이미지
                        String productImageUrl = item.getProduct().getProductImages().stream()
                                .sorted(Comparator.comparingInt(img -> Optional.ofNullable(img.getImageSortOrder()).orElse(9999)))
                                .map(AdminProductImageEntity::getImageUrl)
                                .findFirst()
                                .orElse(null);

                        // 옵션 매핑
                        List<OrderOptionResponseDto> optionDtos = item.getOptions().stream()
                                .map(opt -> OrderOptionResponseDto.builder()
                                        .optionId(opt.getProductOption().getOptionId())
                                        .optionValue(opt.getProductOption().getOptionValue())
                                        .additionalPrice(opt.getAdditionalPrice())
                                        .quantity(opt.getQuantity())
                                        .build())
                                .toList();

                        // 추가구성품 매핑
                        List<OrderAdditionalResponseDto> additionalDtos = item.getAdditionals().stream()
                                .map(add -> OrderAdditionalResponseDto.builder()
                                        .additionalId(add.getProductAdditional().getProductAdditionalId())
                                        .additionalName(add.getProductAdditional().getAdditional().getAdditionalName())
                                        .additionalPrice(add.getAdditionalPrice())
                                        .quantity(add.getQuantity())
                                        .additionalImageUrl(add.getProductAdditional().getAdditional().getAdditionalImageUrl())
                                        .build())
                                .toList();

                        // 사은품 매핑
                        List<OrderGiftResponseDto> giftDtos = item.getGifts().stream()
                                .map(gift -> OrderGiftResponseDto.builder()
                                        .giftId(gift.getProductGift().getProductGiftId())
                                        .giftName(gift.getProductGift().getGift().getGiftName())
                                        .quantity(gift.getQuantity())
                                        .giftImageUrl(gift.getProductGift().getGift().getGiftImageUrl())
                                        .build())
                                .toList();

                        return OrderItemResponseDto.builder()
                                .productId(item.getProduct().getProductId())
                                .productName(item.getProduct().getProductName())
                                .productImageUrl(productImageUrl)
                                .quantity(item.getQuantity())
                                .unitPrice(item.getUnitPrice())
                                .totalPrice(item.getTotalPrice())
                                .options(optionDtos)
                                .additionals(additionalDtos)
                                .gifts(giftDtos)
                                .build();
                    })
                    .toList();

            // 최종 주문 DTO
            return OrderResponseDto.builder()
                    .orderId(order.getOrderId())
                    .orderNumber(order.getOrderNumber())
                    .orderStatus(order.getOrderStatus().name())
                    .originalAmount(order.getOriginalAmount())
                    .discountAmount(order.getDiscountAmount())
                    .totalAmount(order.getTotalAmount())
                    .deliveryFee(order.getDeliveryFee())
                    .payAmount(order.getTotalAmount())
                    .receiverName(order.getReceiverName())
                    .receiverPhone(order.getReceiverPhone())
                    .receiverAddress(order.getReceiverAddress())
                    .receiverMemo(order.getReceiverMemo())
                    .createdAt(order.getCreatedAt())
                    .items(responseItems)
                    .build();
        });
    }


}
