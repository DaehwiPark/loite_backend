package com.boot.loiteBackend.web.user.event;

import com.boot.loiteBackend.domain.mileage.outbok.repository.MileageOutboxRepository;
import com.boot.loiteBackend.web.mileage.outbox.entity.MileageOutboxEntity;
import com.boot.loiteBackend.web.mileage.outbox.model.MileageOutboxEventType;
import com.boot.loiteBackend.web.mileage.outbox.model.MileageOutboxStatus;
import com.boot.loiteBackend.web.mileage.policy.entity.MileagePolicyEntity;
import com.boot.loiteBackend.web.mileage.policy.repository.MileagePolicyRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Slf4j
@Component
@RequiredArgsConstructor
public class MileageEventHandler {

    private final MileagePolicyRepository mileagePolicyRepository;
    private final MileageOutboxRepository mileageOutboxRepository;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void handleUserSignedUp(UserSignedUpEvent event) {
        Long userId = event.getUserId();

        MileagePolicyEntity policy = mileagePolicyRepository
                .findByMileagePolicyCodeAndMileagePolicyYn("SIGN_UP_BONUS", "Y")
                .orElseThrow(() -> new IllegalStateException("회원가입 마일리지 정책이 존재하지 않습니다."));

        MileageOutboxEntity outbox = MileageOutboxEntity.builder()
                .userId(userId)
                .policyId(policy.getMileagePolicyId())
                .eventType(MileageOutboxEventType.SIGN_UP_BONUS)
                .status(MileageOutboxStatus.PENDING)
                .retryCount(0)
                .build();

        mileageOutboxRepository.save(outbox);
        mileageOutboxRepository.flush();

        log.info("회원가입 마일리지 이벤트 Outbox에 저장 완료: userId={}, policyId={}", userId, policy.getMileagePolicyId());
    }
}