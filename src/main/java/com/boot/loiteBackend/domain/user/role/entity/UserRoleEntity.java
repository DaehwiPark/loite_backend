package com.boot.loiteBackend.domain.user.role.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Table(name = "tb_user_role_code")
@EntityListeners(AuditingEntityListener.class)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Schema(description = "사용자 역할 코드")
public class UserRoleEntity {

    @Id
    @Column(name = "ROLE_CODE", length = 20, columnDefinition = "VARCHAR(20) COMMENT '역할 코드 (예: USER, ADMIN)'")
    @Schema(description = "역할 코드", example = "ADMIN")
    private String roleCode;

    @Column(name = "ROLE_NAME", length = 50, nullable = false, columnDefinition = "VARCHAR(50) NOT NULL COMMENT '역할 이름 (화면에 표시될 이름)'")
    @Schema(description = "역할 이름", example = "관리자")
    private String roleName;

    @Column(name = "DESCRIPTION", columnDefinition = "TEXT DEFAULT NULL COMMENT '역할 설명'")
    @Schema(description = "상세 설명", example = "사이트를 관리할 수 있는 관리자 권한")
    private String description;

    @Column(name = "DISPLAY_ORDER", columnDefinition = "INT DEFAULT 0 COMMENT '정렬 순서'")
    @Schema(description = "정렬 순서", example = "1")
    private Integer displayOrder;

    @Column(name = "ACTIVE_YN", columnDefinition = "TINYINT(1) DEFAULT 1 COMMENT '사용 여부'")
    @Schema(description = "사용 여부", example = "true")
    private Boolean active;

    @CreatedDate
    @Column(name = "CREATED_AT", nullable = false, columnDefinition = "TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '생성일시'")
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "UPDATED_AT", nullable = false, columnDefinition = "TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '수정일시'")
    private LocalDateTime updatedAt;
}