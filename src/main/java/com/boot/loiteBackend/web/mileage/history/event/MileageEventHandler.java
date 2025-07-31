package com.boot.loiteBackend.web.mileage.history.event;

import com.boot.loiteBackend.web.mileage.history.service.MileageHistoryService;
import com.boot.loiteBackend.web.mileage.policy.entity.MileagePolicyEntity;
import com.boot.loiteBackend.web.mileage.policy.respository.MileagePolicyRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;
import org.springframework.transaction.event.TransactionPhase;

@Slf4j
@Component
@RequiredArgsConstructor
public class MileageEventHandler {

    private final MileagePolicyRepository mileagePolicyRepository;
    private final MileageHistoryService mileageHistoryService;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleUserSignedUp(UserSignUpEvent event) {
        Long userId = event.getUserId();

        MileagePolicyEntity policy = mileagePolicyRepository
                .findByMileagePolicyCodeAndMileagePolicyYn("SIGN_UP_BONUS", "Y")
                .orElseThrow(() -> new IllegalStateException("회원가입 마일리지 정책이 존재하지 않습니다."));

        // FIXED 방식만 처리
        if ("FIXED".equalsIgnoreCase(policy.getMileagePolicyType())) {
            int point = policy.getMileagePolicyValue();
            String reason = policy.getMileagePolicyName(); // e.g. "회원가입 적립"
            mileageHistoryService.earnSignupMileage(userId, point, reason, policy.getMileagePolicyId());
        } else {
            log.warn("현재 정책은 FIXED(정액) 방식만 지원됩니다. 정책 타입: {}", policy.getMileagePolicyType());
        }
    }
}