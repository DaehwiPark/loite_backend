package com.boot.loiteBackend.web.user.address.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@Builder
@Schema(description = "사용자 배송지 DTO")
public class UserAddressDto {

    @Schema(description = "배송지 ID (고유 식별자)", example = "1")
    private Long id;

    @Schema(description = "해당 배송지를 등록한 사용자 ID", example = "279")
    private Long userId;

    @Schema(description = "배송지 별칭(예: 집, 회사 등)", example = "집")
    private String alias;

    @Schema(description = "수령인 이름", example = "홍길동")
    private String receiverName;

    @Schema(description = "수령인 연락처", example = "010-1234-5678")
    private String receiverPhone;

    @Schema(description = "우편번호", example = "06236")
    private String zipCode;

    @Schema(description = "주소 1 (도로명 주소)", example = "서울 강남구 테헤란로 123")
    private String addressLine1;

    @Schema(description = "주소 2 (상세 주소)", example = "101동 1001호")
    private String addressLine2;

    @JsonProperty("defaultYn")
    @Schema(description = "기본 배송지 여부", example = "N")
    private String defaultYn;

    @JsonProperty("deleteYn")
    @Schema(description = "삭제 여부 (소프트 삭제 처리 여부)", example = "N")
    private String deleteYn;

    @Schema(description = "배송지 등록일시", example = "2025-08-20T09:00:00Z")
    private Instant createdAt;

    @Schema(description = "배송지 수정일시 ", example = "2025-08-20T09:10:00Z")
    private Instant updatedAt;
}
