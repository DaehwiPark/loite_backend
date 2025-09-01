package com.boot.loiteBackend.web.order.service;

import com.boot.loiteBackend.admin.product.option.entity.AdminProductOptionEntity;
import com.boot.loiteBackend.admin.product.option.repository.AdminProductOptionRepository;
import com.boot.loiteBackend.admin.product.product.entity.AdminProductEntity;
import com.boot.loiteBackend.admin.product.product.repository.AdminProductRepository;
import com.boot.loiteBackend.domain.user.address.entity.UserAddressEntity;
import com.boot.loiteBackend.web.order.dto.OrderItemRequestDto;
import com.boot.loiteBackend.web.order.dto.OrderItemResponseDto;
import com.boot.loiteBackend.web.order.dto.OrderRequestDto;
import com.boot.loiteBackend.web.order.dto.OrderResponseDto;
import com.boot.loiteBackend.web.order.entity.OrderEntity;
import com.boot.loiteBackend.web.order.entity.OrderItemEntity;
import com.boot.loiteBackend.web.order.enums.OrderStatus;
import com.boot.loiteBackend.web.delivery.enums.DeliveryStatus;
import com.boot.loiteBackend.web.order.repository.OrderItemRepository;
import com.boot.loiteBackend.web.order.repository.OrderRepository;
import com.boot.loiteBackend.web.order.repository.OrderSequenceRepository;
import com.boot.loiteBackend.web.user.address.dto.UserAddressCreateDto;
import com.boot.loiteBackend.web.user.address.repository.UserAddressRepository;
import com.boot.loiteBackend.web.user.address.service.UserAddressService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final AdminProductRepository productRepository;
    private final AdminProductOptionRepository optionRepository;
    private final OrderSequenceRepository orderSequenceRepository;
    private final UserAddressRepository userAddressRepository;
    private final UserAddressService userAddressService;

    @Override
    @Transactional
    public OrderResponseDto createOrder(Long userId, OrderRequestDto requestDto) {

       /* if(requestDto.getAddressId() == null){
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
        }*/

        // 1. 주문번호 생성
        String date = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        orderSequenceRepository.insertSeq(date);
        Long seq = orderSequenceRepository.getLastSeq();
        String orderNumber = "ORD-" + date + "-" + String.format("%06d", seq);

        // 2. 총액 계산을 위한 변수
        BigDecimal originalTotal = BigDecimal.ZERO;
        BigDecimal discountedTotal = BigDecimal.ZERO;

        // 3. 주문 엔티티 생성 (상품 계산 전에 기본값 세팅)
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

        // 4. 주문 상품 리스트 처리
        List<OrderItemEntity> orderItems = new ArrayList<>();

        for (OrderItemRequestDto itemDto : requestDto.getOrderItems()) {
            // 4-1. 상품 조회
            AdminProductEntity product = productRepository.findById(itemDto.getProductId())
                    .orElseThrow(() -> new IllegalArgumentException("상품을 찾을 수 없습니다."));

            // 4-2. 옵션 조회 (있으면)
            AdminProductOptionEntity option = null;
            if (itemDto.getOptionId() != null) {
                option = optionRepository.findById(itemDto.getOptionId())
                        .orElseThrow(() -> new IllegalArgumentException("옵션을 찾을 수 없습니다."));
            }

            // 4-3. 단가 계산 (상품가격 + 옵션 추가금액)
            BigDecimal unitPrice = product.getProductPrice();
            if (option != null && option.getOptionAdditionalPrice() != null) {
                unitPrice = unitPrice.add(BigDecimal.valueOf(option.getOptionAdditionalPrice()));
            }

            // 4-4. 할인 적용가 (RequestDto에 들어온 할인 금액이 있으면 반영)
            BigDecimal discountedPrice = product.getDiscountedPrice();
            if (option != null && option.getOptionAdditionalPrice() != null) {
                discountedPrice = discountedPrice.add(BigDecimal.valueOf(option.getOptionAdditionalPrice()));
            }

            // 4-5. 총액 = 수량 × 할인단가
            BigDecimal itemOriginalTotal = unitPrice.multiply(BigDecimal.valueOf(itemDto.getQuantity()));
            BigDecimal itemDiscountedTotal = discountedPrice.multiply(BigDecimal.valueOf(itemDto.getQuantity()));

            // 4-6. 주문 아이템 엔티티 생성
            OrderItemEntity orderItem = OrderItemEntity.builder()
                    .order(order)
                    .product(product)
                    .option(option)
                    .quantity(itemDto.getQuantity())
                    .unitPrice(unitPrice)
                    .discountedPrice(discountedPrice)
                    .totalPrice(itemDiscountedTotal)
                    .createdAt(LocalDateTime.now())
                    .build();

            orderItems.add(orderItem);

            // 4-7. 누적 합산
            originalTotal = originalTotal.add(itemOriginalTotal);
            discountedTotal = discountedTotal.add(itemDiscountedTotal);
        }

        // 5. 아이템들 저장
        orderItemRepository.saveAll(orderItems);

        BigDecimal deliveryFee = BigDecimal.ZERO;
        if (discountedTotal.compareTo(new BigDecimal("50000")) < 0) {
            deliveryFee = new BigDecimal("3000"); // 기본 배송비
        }

        // 6. 주문 총액/할인액 갱신 후 다시 저장
        BigDecimal discountAmount = originalTotal.subtract(discountedTotal);
        BigDecimal finalPayAmount = discountedTotal.add(deliveryFee);

        order.setOriginalAmount(originalTotal);
        order.setDiscountAmount(discountAmount);
        order.setTotalAmount(discountedTotal.add(deliveryFee));
        order.setDeliveryFee(deliveryFee);
        order.setUpdatedAt(LocalDateTime.now());

        orderRepository.save(order);

        // 7. Response DTO 변환
        List<OrderItemResponseDto> responseItems = orderItems.stream()
                .map(oi -> OrderItemResponseDto.builder()
                        .productId(oi.getProduct().getProductId())
                        .productName(oi.getProduct().getProductName())
                        .optionId(oi.getOption() != null ? oi.getOption().getOptionId() : null)
                        .optionValue(oi.getOption() != null ? oi.getOption().getOptionValue() : null)
                        .quantity(oi.getQuantity())
                        .unitPrice(oi.getUnitPrice())
                        .totalPrice(oi.getTotalPrice())
                        .build())
                .toList();

        return OrderResponseDto.builder()
                .orderId(order.getOrderId())
                .orderNumber(order.getOrderNumber())
                .orderStatus(order.getOrderStatus().name())
                .originalAmount(originalTotal)
                .discountAmount(discountAmount)
                .totalAmount(order.getTotalAmount())
                .deliveryFee(order.getDeliveryFee())
                .payAmount(finalPayAmount)
                .receiverName(order.getReceiverName())
                .receiverPhone(order.getReceiverPhone())
                .receiverAddress(order.getReceiverAddress())
                .createdAt(order.getCreatedAt())
                .items(responseItems)
                .build();
    }

    @Override
    @Transactional
    public OrderResponseDto getOrder(Long userId, Long orderId) {
        OrderEntity order = orderRepository.findByOrderIdAndUserId(orderId, userId)
                .orElseThrow(() ->
                        new IllegalArgumentException("주문을 찾을 수 없습니다.")
                );

        List<OrderItemEntity> items = orderItemRepository.findByOrder(order);

        List<OrderItemResponseDto> responseItems = items.stream()
                .map(item -> OrderItemResponseDto.builder()
                        .productId(item.getProduct().getProductId())
                        .productName(item.getProduct().getProductName())
                        .optionId(item.getOption() != null ? item.getOption().getOptionId() : null)
                        .optionValue(item.getOption() != null ? item.getOption().getOptionValue() : null)
                        .quantity(item.getQuantity())
                        .unitPrice(item.getUnitPrice())
                        .totalPrice(item.getTotalPrice())
                        .build())
                .toList();

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
    }

    public List<OrderResponseDto> getOrders(Long userId) {
        List<OrderEntity> orders = orderRepository.findByUserId(userId);

        return orders.stream().map(order -> {
            List<OrderItemEntity> items = orderItemRepository.findByOrder(order);

            List<OrderItemResponseDto> responseItems = items.stream()
                    .map(item -> OrderItemResponseDto.builder()
                            .productId(item.getProduct().getProductId())
                            .productName(item.getProduct().getProductName())
                            .optionId(item.getOption() != null ? item.getOption().getOptionId() : null)
                            .optionValue(item.getOption() != null ? item.getOption().getOptionValue() : null)
                            .quantity(item.getQuantity())
                            .unitPrice(item.getUnitPrice())
                            .totalPrice(item.getTotalPrice())
                            .build())
                    .toList();

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
        }).toList();
    }
}
