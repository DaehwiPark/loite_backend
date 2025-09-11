package com.boot.loiteBackend.domain.home.best.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import org.hibernate.annotations.Check;
import org.hibernate.annotations.Generated;
import org.hibernate.annotations.GenerationTime;

import java.time.LocalDateTime;

@Schema(name = "HomeBestItem", description = "홈 인기상품 아이템(최대 10개, 슬롯 1~10)")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(
        name = "tb_home_best_item",
        uniqueConstraints = @UniqueConstraint(
                name = "UX_HOME_BEST_ITEM_SLOT",
                columnNames = {"HOME_BEST_ITEM_SLOT_NO"}
        ),
        indexes = {
                @Index(name = "IX_HOME_BEST_ITEM_PRODUCT", columnList = "PRODUCT_ID")
        }
)
@Check(constraints = "DISPLAY_YN in ('Y','N')")
public class HomeBestItemEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "HOME_BEST_ITEM_ID",
            columnDefinition = "BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT 'PK'")
    private Long id;

    @Column(name = "PRODUCT_ID",
            columnDefinition = "BIGINT(20) DEFAULT NULL COMMENT 'FK: 상품 ID(선택)'")
    private Long productId;

    @NotNull
    @Min(1) @Max(10)
    @Column(name = "HOME_BEST_ITEM_SLOT_NO",
            nullable = false,
            columnDefinition = "TINYINT(4) NOT NULL COMMENT '노출 슬롯(1~10)'")
    private Integer slotNo;

    @NotBlank
    @Pattern(regexp = "Y|N")
    @Column(name = "DISPLAY_YN",
            nullable = false,
            length = 1,
            columnDefinition = "CHAR(1) NOT NULL DEFAULT 'Y' COMMENT '노출(Y/N)'")
    private String displayYn;

    @Column(name = "CREATED_BY",
            columnDefinition = "BIGINT(20) DEFAULT NULL COMMENT '생성자'")
    private Long createdBy;

    @Column(name = "UPDATED_BY",
            columnDefinition = "BIGINT(20) DEFAULT NULL COMMENT '수정자'")
    private Long updatedBy;

    @Generated(GenerationTime.INSERT)
    @Column(name = "CREATED_AT",
            nullable = false, insertable = false, updatable = false,
            columnDefinition = "DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '생성일'")
    private LocalDateTime createdAt;

    @Generated(GenerationTime.ALWAYS)
    @Column(name = "UPDATED_AT",
            nullable = false, insertable = false, updatable = false,
            columnDefinition = "DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '수정일'")
    private LocalDateTime updatedAt;
}
