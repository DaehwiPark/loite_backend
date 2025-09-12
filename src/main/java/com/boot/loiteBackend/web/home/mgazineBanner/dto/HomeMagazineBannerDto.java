package com.boot.loiteBackend.web.home.mgazineBanner.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Schema(description = "홈 매거진 배너 공개 조회 DTO")
public class HomeMagazineBannerDto {

    private Long bannerId;
    private Long productId;
    private String videoThumbnailUrl;
    private String videoUrl;
    private String productName;
    private String productModelName;

    private String title;
    private String subtitle;
    private String buttonText;
    private String buttonUrl;

    private String image1; // 썸네일 1
    private String image2; // 썸네일 2

    // JPQL 생성자 프로젝션용 생성자 (파라미터 순서 반드시 일치)
    public HomeMagazineBannerDto(Long bannerId,
                                 Long productId,
                                 String videoThumbnailUrl,
                                 String videoUrl,
                                 String productName,
                                 String productModelName,
                                 String title,
                                 String subtitle,
                                 String buttonText,
                                 String buttonUrl,
                                 String image1,
                                 String image2) {
        this.videoThumbnailUrl = videoThumbnailUrl;
        this.videoUrl = videoUrl;
        this.bannerId = bannerId;
        this.productId = productId;
        this.productName = productName;
        this.productModelName = productModelName;
        this.title = title;
        this.subtitle = subtitle;
        this.buttonText = buttonText;
        this.buttonUrl = buttonUrl;
        this.image1 = image1;
        this.image2 = image2;
    }
}