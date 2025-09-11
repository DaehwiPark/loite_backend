package com.boot.loiteBackend.web.home.best.dto;

import lombok.*;
import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HomeBestItemResponseDto {

    private Integer slotNo;
    private Long productId;
    private String productName;
    private String productModelName;
    private String productSummary;
    private BigDecimal productPrice;
    private Long categoryId;
    private String depth1Path;
    private String depth2Path;
    private String depth3Path;
    private String imageUrl;

    // JPQL constructor expression
    public HomeBestItemResponseDto(
            String productName,
            String productModelName,
            String productSummary,
            BigDecimal productPrice,
            Integer slotNo,
            Long productId,
            Long categoryId,
            String depth1Path,
            String depth2Path,
            String depth3Path,
            String imageUrl
    ) {
        this.productName = productName;
        this.productModelName = productModelName;
        this.productSummary = productSummary;
        this.productPrice = productPrice;
        this.slotNo = slotNo;
        this.productId = productId;
        this.categoryId = categoryId;
        this.depth1Path = depth1Path;
        this.depth2Path = depth2Path;
        this.depth3Path = depth3Path;
        this.imageUrl = imageUrl;
    }
}
