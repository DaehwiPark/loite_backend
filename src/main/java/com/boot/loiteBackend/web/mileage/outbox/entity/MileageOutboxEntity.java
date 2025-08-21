package com.boot.loiteBackend.web.mileage.outbox.entity;

import com.boot.loiteBackend.web.mileage.outbox.model.MileageOutboxEventType;
import com.boot.loiteBackend.web.mileage.outbox.model.MileageOutboxStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "tb_mileage_outbox")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "마일리지 적립 Outbox 이벤트 엔티티")
public class MileageOutboxEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Outbox 이벤트 고유 ID")
    private Long id;

    @Column(name = "user_id", nullable = false)
    @Schema(description = "마일리지 적립 대상 사용자 ID")
    private Long userId;

    @Column(name = "policy_id", nullable = false)
    @Schema(description = "적용된 마일리지 정책 ID")
    private Long policyId;

    @Enumerated(EnumType.STRING)
    @Column(name = "mileage_outbox_event_type", nullable = false, length = 50)
    @Schema(description = "이벤트 타입 (예: SIGN_UP_BONUS)")
    private MileageOutboxEventType eventType;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    @Column(name = "mileage_outbox_status", nullable = false, length = 20)
    @Schema(description = "처리 상태 (PENDING, SUCCESS, FAILED)")
    private MileageOutboxStatus status = MileageOutboxStatus.PENDING;

    @Column(name = "mileage_outbox_retry_count")
    @Schema(description = "재시도 횟수")
    @Builder.Default
    private Integer retryCount = 0;

    @Column(name = "created_at", nullable = false, insertable = false, updatable = false,
            columnDefinition = "datetime DEFAULT CURRENT_TIMESTAMP")
    @Schema(description = "이벤트가 생성된 시간")
    private LocalDateTime createdAt;

    @Column(name = "last_attempt_at")
    @Schema(description = "최근 이벤트 처리 시도 시간")
    private LocalDateTime lastAttemptAt;
}