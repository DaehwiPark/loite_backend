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

    @Schema(description = "수령인 주소", example = "서울 강남구 테헤란로 123")
    private String receiverAddress;

    @Schema(description = "결제수단", example = "CARD")
    private String paymentMethod;

    @Schema(description = "사용 마일리지 (없으면 0)", example = "0")
    private Integer usedMileage;

    @Schema(description = "주문 상품 리스트")
    private List<OrderItemRequestDto> orderItems;
}

