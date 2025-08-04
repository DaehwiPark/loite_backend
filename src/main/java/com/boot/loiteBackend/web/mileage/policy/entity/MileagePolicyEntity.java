package com.boot.loiteBackend.web.mileage.policy.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "tb_mileage_policy")
@Schema(description = "마일리지 적립 정책 엔티티")
public class MileagePolicyEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MILEAGE_POLICY_ID")
    @Schema(description = "정책 고유 ID", example = "1")
    private Long mileagePolicyId;

    @Column(name = "MILEAGE_POLICY_CODE", nullable = false, length = 50, unique = true)
    @Schema(description = "정책 코드 (예: SIGN_UP_BONUS)", example = "SIGN_UP_BONUS")
    private String mileagePolicyCode;

    @Column(name = "MILEAGE_POLICY_NAME", nullable = false, length = 50)
    @Schema(description = "정책 이름", example = "회원가입 적립")
    private String mileagePolicyName;

    @Column(name = "MILEAGE_POLICY_TYPE", nullable = false, length = 20)
    @Schema(description = "적립 방식: FIXED=정액, PERCENTAGE=비율", example = "FIXED")
    private String mileagePolicyType;

    @Column(name = "MILEAGE_POLICY_VALUE", nullable = false)
    @Schema(description = "정액 포인트 또는 비율 값", example = "1000")
    private Integer mileagePolicyValue;

    @Column(name = "MILEAGE_POLICY_UNIT", length = 10)
    @Schema(description = "단위: point 또는 %", example = "point")
    private String mileagePolicyUnit;

    @Column(name = "MILEAGE_POLICY_CONDITION", nullable = false, length = 50)
    @Schema(description = "정책 적용 조건 코드 (예: SIGN_UP)", example = "SIGN_UP")
    private String mileagePolicyCondition;

    @Column(name = "MILEAGE_POLICY_YN", nullable = false, length = 1)
    @Schema(description = "정책 활성화 여부 (Y/N)", example = "Y")
    private String mileagePolicyYn;

    @Column(name = "CREATED_AT", nullable = false, updatable = false, insertable = false,
            columnDefinition = "timestamp default current_timestamp")
    @Schema(description = "정책 생성일시", example = "2024-01-01T00:00:00")
    private java.sql.Timestamp createdAt;

    @Column(name = "UPDATED_AT", nullable = false, insertable = false,
            columnDefinition = "timestamp default current_timestamp on update current_timestamp")
    @Schema(description = "정책 수정일시", example = "2024-01-01T00:00:00")
    private java.sql.Timestamp updatedAt;
}