package com.boot.loiteBackend.web.user.status.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "tb_user_status_code")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Schema(description = "사용자 상태 코드")
public class UserStatusEntity {

    @Id
    @Column(name = "STATUS_CODE", length = 20)
    @Schema(description = "상태 코드", example = "ACTIVE")
    private String statusCode;

    @Column(name = "STATUS_NAME", length = 50, nullable = false)
    @Schema(description = "상태 이름", example = "활동 계정")
    private String statusName;

    @Column(name = "DESCRIPTION")
    @Schema(description = "상세 설명", example = "일반적으로 로그인/활동 가능한 사용자 계정")
    private String description;

    @Column(name = "DISPLAY_ORDER")
    @Schema(description = "정렬 순서", example = "1")
    private Integer displayOrder;

    @Column(name = "ACTIVE_YN")
    @Schema(description = "사용 여부", example = "true")
    private Boolean active;
}