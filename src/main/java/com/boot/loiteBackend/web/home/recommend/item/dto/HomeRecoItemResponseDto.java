package com.boot.loiteBackend.web.home.recommend.item.dto;

import lombok.*;
import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HomeRecoItemResponseDto {

    // item 영역
    private Long sectionId;
    private Integer slotNo;

    // product 영역
    private Long productId;
    private String productName;
    private String productModelName;
    private String productSummary;
    private BigDecimal productPrice;
    private boolean bestProductYn;
    private boolean newProductYn;

    // category 영역
    private Long categoryId;
    private String depth1Path;
    private String depth2Path;
    private String depth3Path;

    private String imageUrl;

    /**
     * JPQL constructor expression과 일치하는 생성자
     */
    public HomeRecoItemResponseDto(
            Long sectionId,
            String productName,
            String productModelName,
            String productSummary,
            BigDecimal productPrice,
            Boolean bestProductYn,
            Boolean newProductYn,
            Integer slotNo,
            Long productId,
            Long categoryId,
            String depth1Path,
            String depth2Path,
            String depth3Path,
            String imageUrl
    ) {
        this.sectionId = sectionId;
        this.productName = productName;
        this.productModelName = productModelName;
        this.productSummary = productSummary;
        this.productPrice = productPrice;
        this.bestProductYn = bestProductYn;
        this.newProductYn = newProductYn;
        this.slotNo = slotNo;
        this.productId = productId;
        this.categoryId = categoryId;
        this.depth1Path = depth1Path;
        this.depth2Path = depth2Path;
        this.depth3Path = depth3Path;
        this.imageUrl = imageUrl;
    }
}