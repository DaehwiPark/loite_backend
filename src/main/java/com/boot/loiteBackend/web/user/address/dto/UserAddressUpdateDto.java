package com.boot.loiteBackend.web.user.address.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "사용자 배송지 수정 요청 DTO (부분 업데이트 허용)")
public class UserAddressUpdateDto {

    @Schema(description = "배송지 별칭 (예: 집, 회사, 부모님 댁 등). 최대 50자까지 입력 가능",
            example = "집")
    @Size(max = 50)
    private String alias;

    @Schema(description = "수령인 이름. 최대 50자까지 입력 가능",
            example = "홍길동")
    @Size(max = 50)
    private String receiverName;

    @Schema(description = "수령인 연락처. 최대 20자까지 입력 가능",
            example = "010-1234-5678")
    @Size(max = 20)
    private String receiverPhone;

    @Schema(description = "우편번호. 최대 10자까지 입력 가능",
            example = "06236")
    @Size(max = 10)
    private String zipCode;

    @Schema(description = "기본 주소 (도로명 주소). 최대 200자까지 입력 가능",
            example = "서울 강남구 테헤란로 123")
    @Size(max = 200)
    private String addressLine1;

    @Schema(description = "상세 주소 (선택). 최대 200자까지 입력 가능",
            example = "101동 1001호")
    @Size(max = 200)
    private String addressLine2;

    @JsonProperty("defaultYn")
    @Schema(description = "기본 배송지 여부. Y면 이 주소를 기본으로 지정, N면 기본 해제. " +
            "null일 경우 변경하지 않음",
            example = "N")
    private String defaultYn;
}
