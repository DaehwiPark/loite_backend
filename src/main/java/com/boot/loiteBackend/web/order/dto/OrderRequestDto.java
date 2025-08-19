package com.boot.loiteBackend.web.order.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderRequestDto {

    @Schema(description = "수령인 이름", example = "홍길동")
    private String receiverName;

    @Schema(description = "수령인 연락처", example = "010-1234-5678")
    private String receiverPhone;

    @Schema(description = "수령인 주소", example = "서울특별시 강남구 테헤란로 123")
    private String receiverAddress;

    @Schema(description = "결제 수단", example = "CARD") // CARD, KAKAOPAY, VIRTUAL_ACCOUNT 등
    private String paymentMethod;

    @Schema(description = "사용 마일리지", example = "1000") // 0이면 미사용
    private int usedMileage;

    @Schema(description = "최종 결제 금액", example = "59000")
    private BigDecimal totalAmount;

    @Schema(description = "할인 금액", example = "1000")
    private BigDecimal discountAmount;

    @Schema(description = "배송비", example = "3000")
    private BigDecimal deliveryFee;

    @Schema(description = "주문 상품 목록")
    private List<OrderItemRequestDto> orderItems;
}

