package com.boot.loiteBackend.web.mileage.history.entity;

import com.boot.loiteBackend.web.mileage.history.model.MileageHistoryType;
import jakarta.persistence.*;
        import lombok.*;
        import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "tb_mileage_history")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MileageHistoryEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MILEAGE_HISTORY_ID", columnDefinition = "bigint(20) NOT NULL AUTO_INCREMENT COMMENT '마일리지 고유 ID'")
    private Long mileageHistoryId;

    @Column(name = "USER_ID", nullable = false, columnDefinition = "bigint(20) NOT NULL COMMENT '회원 ID (마일리지 소유자)'")
    private Long userId;

    @Enumerated(EnumType.STRING)
    @Column(name = "MILEAGE_HISTORY_TYPE", nullable = false, length = 20, columnDefinition = "varchar(20) NOT NULL COMMENT '타입: EARN=적립, USE=사용, EXPIRE=소멸'")
    private MileageHistoryType mileageHistoryType;

    @Column(name = "MILEAGE_HISTORY_SOURCE", length = 50, columnDefinition = "varchar(50) DEFAULT NULL COMMENT '적립/사용 사유: 주문번호, 회원가입 등'")
    private String mileageHistorySource;

    @Column(name = "MILEAGE_HISTORY_AMOUNT", nullable = false, columnDefinition = "int(11) NOT NULL COMMENT '마일리지 변동량 (+적립, -사용)'")
    private Integer mileageHistoryAmount;

    @Column(name = "MILEAGE_HISTORY_TOTAL_AMOUNT", columnDefinition = "int(11) DEFAULT 0 COMMENT '해당 시점의 마일리지 잔액'")
    private Integer mileageHistoryTotalAmount;

    @Column(name = "MILEAGE_POLICY_ID", columnDefinition = "bigint(20) DEFAULT NULL COMMENT '적립(EARN)인 경우 적용된 마일리지 정책 ID'")
    private Long mileagePolicyId;

    @Column(name = "ORDER_ID", columnDefinition = "bigint(20) DEFAULT NULL COMMENT '주문과 연관된 경우 주문 ID'")
    private Long orderId;

    @CreationTimestamp
    @Column(name = "CREATED_AT", nullable = false,
            updatable = false,
            columnDefinition = "timestamp NOT NULL DEFAULT current_timestamp COMMENT '적립/사용 발생 일시'")
    private LocalDateTime createdAt;
}
