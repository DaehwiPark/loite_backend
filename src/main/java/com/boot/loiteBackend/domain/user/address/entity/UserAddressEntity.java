package com.boot.loiteBackend.domain.user.address.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Instant;

@Entity
@Table(name = "tb_user_address",
        indexes = {
                @Index(name = "IDX_USER_ID", columnList = "USER_ID"),
                @Index(name = "IDX_ZIP_CODE", columnList = "USER_ADDRESS_ZIP_CODE")
        })
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
public class UserAddressEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "USER_ADDRESS_ID", columnDefinition = "BIGINT AUTO_INCREMENT COMMENT '주소 PK'")
    private Long id;

    @Column(name = "USER_ID", nullable = false, columnDefinition = "BIGINT NOT NULL COMMENT '사용자 ID'")
    private Long userId;

    @Column(name = "USER_ADDRESS_ALIAS", length = 50, columnDefinition = "VARCHAR(50) COMMENT '주소 별칭'")
    private String alias;

    @Column(name = "USER_ADDRESS_RECEIVER_NAME", nullable = false, length = 50, columnDefinition = "VARCHAR(50) NOT NULL COMMENT '수령인 이름'")
    private String receiverName;

    @Column(name = "USER_ADDRESS_RECEIVER_PHONE", nullable = false, length = 20, columnDefinition = "VARCHAR(20) NOT NULL COMMENT '수령인 전화번호'")
    private String receiverPhone;

    @Column(name = "USER_ADDRESS_ZIP_CODE", nullable = false, length = 10, columnDefinition = "VARCHAR(10) NOT NULL COMMENT '우편번호'")
    private String zipCode;

    @Column(name = "USER_ADDRESS_LINE1", nullable = false, length = 200, columnDefinition = "VARCHAR(200) NOT NULL COMMENT '기본 주소'")
    private String addressLine1;

    @Column(name = "USER_ADDRESS_LINE2", length = 200, columnDefinition = "VARCHAR(200) COMMENT '상세 주소'")
    private String addressLine2;

    /** Y/N 플래그 컬럼들 */
    @Builder.Default
    @Column(name = "DEFAULT_YN", nullable = false, length = 1,
            columnDefinition = "CHAR(1) NOT NULL DEFAULT 'N' COMMENT '기본 배송지 여부(Y/N)'")
    private String defaultYn = "N";

    @Builder.Default
    @Column(name = "DELETE_YN", nullable = false, length = 1,
            columnDefinition = "CHAR(1) NOT NULL DEFAULT 'N' COMMENT '삭제 여부(Y/N)'")
    private String deleteYn = "N";

    @CreatedDate
    @Column(name = "CREATED_AT", nullable = false, updatable = false,
            columnDefinition = "TIMESTAMP NOT NULL COMMENT '생성일시'")
    private Instant createdAt;

    @LastModifiedDate
    @Column(name = "UPDATED_AT", nullable = false,
            columnDefinition = "TIMESTAMP NOT NULL COMMENT '수정일시'")
    private Instant updatedAt;
}