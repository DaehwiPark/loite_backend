package com.boot.loiteBackend.domain.user.status.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Table(name = "tb_user_status_code")
@EntityListeners(AuditingEntityListener.class)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Schema(description = "사용자 상태 코드")
public class UserStatusEntity {

    @Id
    @Column(name = "STATUS_CODE", length = 20, columnDefinition = "VARCHAR(20) COMMENT '상태 코드'")
    @Schema(description = "상태 코드", example = "ACTIVE")
    private String statusCode;

    @Column(name = "STATUS_NAME", length = 50, nullable = false, columnDefinition = "VARCHAR(50) NOT NULL COMMENT '상태 표시 이름'")
    @Schema(description = "상태 이름", example = "활동 계정")
    private String statusName;

    @Column(name = "DESCRIPTION", columnDefinition = "TEXT COMMENT '설명'")
    @Schema(description = "상세 설명", example = "일반적으로 로그인/활동 가능한 사용자 계정")
    private String description;

    @Column(name = "DISPLAY_ORDER", columnDefinition = "INT DEFAULT 0 COMMENT '정렬 순서'")
    @Schema(description = "정렬 순서", example = "1")
    private Integer displayOrder;

    @Column(name = "ACTIVE_YN", columnDefinition = "TINYINT(1) DEFAULT 1 COMMENT '활성 여부'")
    @Schema(description = "사용 여부", example = "true")
    private Boolean active;

    @CreatedDate
    @Column(name = "CREATED_AT", nullable = false,
            columnDefinition = "TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '생성일시'")
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "UPDATED_AT", nullable = false,
            columnDefinition = "TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '수정일시'")
    private LocalDateTime updatedAt;
}