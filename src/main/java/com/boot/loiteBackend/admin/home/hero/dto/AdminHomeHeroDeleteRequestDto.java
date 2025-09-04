package com.boot.loiteBackend.admin.home.hero.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(name = "AdminHomeHeroDeleteRequestDto", description = "메인 히어로 섹션 삭제 요청 DTO")
public class AdminHomeHeroDeleteRequestDto {

    @Schema(description = "HOME_HERO_ID", example = "101", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull
    private Long homeHeroId;

    @Schema(description = "삭제 수행자 USER_ID", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull
    private Long updatedBy;
}
