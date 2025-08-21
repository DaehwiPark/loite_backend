package com.boot.loiteBackend.domain.mileage.outbok.processor;

import com.boot.loiteBackend.domain.mileage.outbok.repository.MileageOutboxRepository;
import com.boot.loiteBackend.web.mileage.history.service.MileageHistoryService;
import com.boot.loiteBackend.web.mileage.outbox.entity.MileageOutboxEntity;
import com.boot.loiteBackend.web.mileage.outbox.model.MileageOutboxStatus;
import com.boot.loiteBackend.web.mileage.policy.entity.MileagePolicyEntity;
import com.boot.loiteBackend.web.mileage.policy.repository.MileagePolicyRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class MileageOutboxProcessor {

    private final MileageOutboxRepository mileageOutboxRepository;
    private final MileagePolicyRepository mileagePolicyRepository;
    private final MileageHistoryService mileageHistoryService;

    private static final int MAX_RETRY = 3;

//    @Scheduled(fixedDelay = 60000)
//    public void processOutboxEvents() {
//        System.out.println("스케줄러 실행");
//        List<MileageOutboxEntity> events = mileageOutboxRepository
//                .findTop10ByStatusInAndRetryCountLessThanOrderByLastAttemptAtAsc(
//                        List.of(MileageOutboxStatus.PENDING, MileageOutboxStatus.FAILED), 3
//                );
//
//        for (MileageOutboxEntity event : events) {
//            processEvent(event);
//        }
//    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void processEvent(MileageOutboxEntity event) {
        try {
            MileagePolicyEntity policy = mileagePolicyRepository.findById(event.getPolicyId())
                    .orElseThrow(() -> new IllegalStateException("해당 정책이 존재하지 않습니다. ID = " + event.getPolicyId()));

            int point = policy.getMileagePolicyValue();
            String reason = policy.getMileagePolicyName();

            mileageHistoryService.earnSignupMileage(
                    event.getUserId(),
                    point,
                    reason,
                    event.getPolicyId()
            );

            event.setStatus(MileageOutboxStatus.SUCCESS);
            event.setLastAttemptAt(LocalDateTime.now());
            mileageOutboxRepository.save(event);

            log.info("Outbox 처리 성공: eventId={}, userId={}", event.getId(), event.getUserId());

        } catch (Exception ex) {
            log.error("Outbox 처리 실패: eventId={}, 원인={}", event.getId(), ex.getMessage(), ex);

            event.setStatus(MileageOutboxStatus.FAILED);
            event.setRetryCount(event.getRetryCount() + 1);
            event.setLastAttemptAt(LocalDateTime.now());
            mileageOutboxRepository.save(event);

            if (event.getRetryCount() >= MAX_RETRY) {
                log.error("⚠️ 최대 재시도 초과: eventId={}, userId={}", event.getId(), event.getUserId());
                // TODO: Slack, Email 등 알림 연동
            }
        }
    }
}