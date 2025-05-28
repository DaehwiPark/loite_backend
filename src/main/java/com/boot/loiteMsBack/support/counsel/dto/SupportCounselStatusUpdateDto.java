package com.boot.loiteMsBack.support.counsel.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Schema(description = "1:1 문의 상태 변경 요청 DTO")
@Getter
@Setter
@NoArgsConstructor
public class SupportCounselStatusUpdateDto {

    @Schema(description = "변경할 상태값", example = "완료")
    private String status;
}
