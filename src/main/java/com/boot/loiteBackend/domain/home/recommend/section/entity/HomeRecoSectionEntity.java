package com.boot.loiteBackend.domain.home.recommend.section.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import java.time.LocalDateTime;

@Schema(name = "HomeRecoSection", description = "홈 추천상품 섹션(좌측 박스)")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
@Entity @Table(name="tb_home_reco_section",
        indexes = {
                @Index(name="IX_RECO_SECTION_SORT", columnList = "SORT_ORDER"),
                @Index(name="IX_RECO_SECTION_DISPLAY", columnList = "DISPLAY_YN")
        }
)
public class HomeRecoSectionEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="HOME_RECO_SECTION_ID")
    @Schema(description="PK", example="1")
    private Long id;

    @NotBlank
    @Column(name="HOME_RECO_TITLE", nullable=false, length=100)
    @Schema(description="좌측 박스 타이틀", example="혼자서도 충분히")
    private String title;

    @Column(name="HOME_RECO_LINK_URL", length=2048)
    @Schema(description="이동 URL", example="/collections/solo")
    private String linkUrl;

    @NotBlank
    @Pattern(regexp = "_self|_blank")
    @Column(name="HOME_RECO_LINK_TARGET", nullable=false, length=10)
    @Schema(description="링크 타겟", allowableValues={"_self","_blank"})
    private String linkTarget;

    @NotBlank
    @Pattern(regexp = "Y|N")
    @Column(name="DISPLAY_YN", nullable=false, length=1)
    @Schema(description="노출 여부", example="Y", allowableValues={"Y","N"})
    private String displayYn;

    @Column(name="SORT_ORDER", nullable=false)
    @Schema(description="섹션 정렬(낮을수록 상단)", example="0")
    private Integer sortOrder;

    @Column(name="CREATED_BY") private Long createdBy;
    @Column(name="UPDATED_BY") private Long updatedBy;

    @Column(name="CREATED_AT", nullable=false, insertable=false, updatable=false)
    private LocalDateTime createdAt;

    @Column(name="UPDATED_AT", nullable=false, insertable=false)
    private LocalDateTime updatedAt;
}
