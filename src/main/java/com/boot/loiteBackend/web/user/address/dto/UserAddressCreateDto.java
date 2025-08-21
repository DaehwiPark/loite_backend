package com.boot.loiteBackend.web.user.address.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "사용자 배송지 등록 요청 DTO")
public class UserAddressCreateDto {

    @Schema(description = "배송지 별칭 (예: 집, 회사, 부모님 댁 등). 최대 50자까지 입력 가능",
            example = "집")
    @Size(max = 50)
    private String alias;

    @Schema(description = "수령인 이름 (필수). 최대 50자까지 입력 가능",
            example = "홍길동", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank @Size(max = 50)
    private String recipientName;

    @Schema(description = "수령인 연락처 (필수). 최대 20자까지 입력 가능",
            example = "010-1234-5678", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank @Size(max = 20)
    private String recipientPhone;

    @Schema(description = "우편번호 (필수). 최대 10자까지 입력 가능",
            example = "06236", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank @Size(max = 10)
    private String zipCode;

    @Schema(description = "기본 주소 (도로명 주소, 필수). 최대 200자까지 입력 가능",
            example = "서울 강남구 테헤란로 123", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank @Size(max = 200)
    private String addressLine1;

    @Schema(description = "상세 주소 (선택). 최대 200자까지 입력 가능",
            example = "101동 1001호")
    @Size(max = 200)
    private String addressLine2;

    @Schema(description = "배송 요청사항 (선택). 최대 200자까지 입력 가능",
            example = "부재시 문 앞에 두세요")
    @Size(max = 200)
    private String deliveryRequest;

    @JsonProperty("isDefault")
    @Schema(description = "기본 배송지 여부. true로 설정하면 기존 기본 배송지를 해제하고 이 주소를 기본으로 등록",
            example = "true")
    private boolean isDefault;
}
