package com.boot.loiteBackend.web.cartItem.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AvailableOptionResponseDto {

    @Schema(description = "옵션 ID(저장 시 사용)", example = "34")
    private Long optionId;

    @Schema(description = "옵션명", example = "화이트")
    private String optionValue;

    @Schema(description = "컬러 코드", example = "#FFFFFF")
    private String colorCode;

    @Schema(description = "수량", example = "2")
    private Integer quantity;
}
