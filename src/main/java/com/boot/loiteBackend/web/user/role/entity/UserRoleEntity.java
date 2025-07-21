package com.boot.loiteBackend.web.user.role.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "tb_user_role_code")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Schema(description = "사용자 역할 코드")
public class UserRoleCodeEntity {

    @Id
    @Column(name = "ROLE_CODE", length = 20)
    @Schema(description = "역할 코드", example = "ADMIN")
    private String roleCode;

    @Column(name = "ROLE_NAME", length = 50, nullable = false)
    @Schema(description = "역할 이름", example = "관리자")
    private String roleName;

    @Column(name = "DESCRIPTION")
    @Schema(description = "상세 설명", example = "사이트를 관리할 수 있는 관리자 권한")
    private String description;

    @Column(name = "DISPLAY_ORDER")
    @Schema(description = "정렬 순서", example = "1")
    private Integer displayOrder;

    @Column(name = "ACTIVE_YN")
    @Schema(description = "사용 여부", example = "true")
    private Boolean active;
}