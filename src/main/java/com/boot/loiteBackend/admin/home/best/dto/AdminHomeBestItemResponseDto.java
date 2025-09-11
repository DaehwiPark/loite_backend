package com.boot.loiteBackend.admin.home.best.dto;

import com.boot.loiteBackend.admin.home.recommend.item.dto.AdminHomeRecoItemResponseDto;
import com.boot.loiteBackend.domain.home.recommend.item.entity.HomeRecoItemEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(name = "AdminHomeBestItemResponseDto", description = "인기 아이템 응답 DTO")
public class AdminHomeBestItemResponseDto {

    @Schema(description = "아이템 ID (PK)", example = "101")
    private Long id;

    @Schema(description = "상품 ID (FK, 선택)", example = "200023")
    private Long productId;

    @Schema(description = "노출 슬롯 번호 (1~10)", example = "3")
    private Integer slotNo;

    @Schema(description = "노출 여부", allowableValues = {"Y", "N"}, example = "Y")
    private String displayYn;

    @Schema(description = "생성자 USER_ID", example = "1001")
    private Long createdBy;

    @Schema(description = "수정자 USER_ID", example = "1002")
    private Long updatedBy;

    @Schema(description = "생성일시", example = "2025-09-08T10:15:30")
    private LocalDateTime createdAt;

    @Schema(description = "수정일시", example = "2025-09-08T11:12:45")
    private LocalDateTime updatedAt;

    public static AdminHomeRecoItemResponseDto fromEntity(HomeRecoItemEntity e) {
        if (e == null) return null;
        return AdminHomeRecoItemResponseDto.builder()
                .id(e.getId())
                .productId(e.getProductId())
                .slotNo(e.getSlotNo())
                .displayYn(e.getDisplayYn())
                .createdBy(e.getCreatedBy())
                .createdAt(e.getCreatedAt())
                .updatedBy(e.getUpdatedBy())
                .updatedAt(e.getUpdatedAt())
                .createdAt(LocalDateTime.now())
                .build();
    }
}