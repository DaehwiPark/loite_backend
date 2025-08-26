package com.boot.loiteBackend.web.order.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
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

    @Schema(description = "배송 요청 사항", example = "고생 많으십니다.")
    private String receiverMemo;

    @Schema(description = "결제수단", example = "CARD")
    private String paymentMethod;

    @Schema(description = "사용 마일리지 (없으면 0)", example = "0")
    private Integer usedMileage;

    @Schema(description = "주문 상품 리스트")
    private List<OrderItemRequestDto> orderItems;

    @Schema(description = "배송지 별칭 (예: 집, 회사, 부모님 댁 등). 최대 50자까지 입력 가능",
            example = "집")
    @Size(max = 50)
    private String alias;

    @Schema(description = "우편번호 (필수). 최대 10자까지 입력 가능",
            example = "06236", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank
    @Size(max = 10)
    private String zipCode;

    @Schema(description = "기본 주소 (도로명 주소, 필수). 최대 200자까지 입력 가능",
            example = "서울 강남구 테헤란로 123", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank @Size(max = 200)
    private String addressLine1;

    @Schema(description = "상세 주소 (선택). 최대 200자까지 입력 가능",
            example = "101동 1001호")
    @Size(max = 200)
    private String addressLine2;

    @JsonProperty("defaultYn")
    @Schema(description = "기본 배송지 여부. Y로 설정하면 기존 기본 배송지를 해제하고 이 주소를 기본으로 등록",
            example = "N")
    private String defaultYn;
}

