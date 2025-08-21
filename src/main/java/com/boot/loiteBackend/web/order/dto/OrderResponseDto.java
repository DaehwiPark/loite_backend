package com.boot.loiteBackend.web.order.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderResponseDto {

    @Schema(description = "주문 PK", example = "1001")
    private Long orderId;

    @Schema(description = "주문번호 (가맹점 고유값)", example = "ORD-20250819-0001")
    private String orderNumber;

    @Schema(description = "주문 상태", example = "PENDING")
    private String orderStatus;

    @Schema(description = "총 결제 금액", example = "120000")
    private BigDecimal totalAmount;

    @Schema(description = "할인 금액", example = "5000")
    private BigDecimal discountAmount;

    @Schema(description = "배송비", example = "3000")
    private BigDecimal deliveryFee;

    @Schema(description = "결제 예정 금액 (총액 - 할인 + 배송비)", example = "118000")
    private BigDecimal payAmount;

    @Schema(description = "배송지 이름", example = "홍길동")
    private String receiverName;

    @Schema(description = "배송지 연락처", example = "010-1234-5678")
    private String receiverPhone;

    @Schema(description = "배송지 주소", example = "서울특별시 강남구 테헤란로 123")
    private String receiverAddress;

    @Schema(description = "주문 생성 일시", example = "2025-08-19T13:45:00")
    private LocalDateTime createdAt;

    @Schema(description = "주문 상품 리스트")
    private List<OrderItemResponseDto> items;
}

