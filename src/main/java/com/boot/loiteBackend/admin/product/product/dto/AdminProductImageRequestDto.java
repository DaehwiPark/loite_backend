package com.boot.loiteBackend.admin.product.product.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class AdminProductImageRequestDto {

    @Schema(description = "이미지 고유 ID (수정 시 필요, 등록 시 null 가능)", example = "101")
    private Long imageId;

    @Schema(description = "이미지가 속한 상품 ID", example = "1001")
    private Long productId;

    @Schema(description = "이미지 접근 URL (CDN 또는 S3 주소)", example = "https://cdn.loite.com/images/product/sofa01.jpg")
    private String imageUrl;

    @Schema(description = "이미지 타입 (예: THUMBNAIL, DETAIL 등)", example = "THUMBNAIL")
    private String imageType;

    @Schema(description = "서버 내 저장된 이미지 경로", example = "/assets/product/image/sofa01.jpg")
    private String imagePath;

    @Schema(description = "이미지 노출 순서 (낮을수록 먼저 노출)", example = "1")
    private int imageSortOrder;

    @Schema(description = "이미지 활성화 여부 (Y/N)", example = "Y")
    private String activeYn;
}

