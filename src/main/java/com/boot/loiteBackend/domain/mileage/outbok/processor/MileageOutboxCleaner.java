package com.boot.loiteBackend.domain.mileage.outbok.processor;

import com.boot.loiteBackend.domain.mileage.outbok.repository.MileageOutboxRepository;
import com.boot.loiteBackend.web.mileage.outbox.model.MileageOutboxStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Slf4j
@Component
@RequiredArgsConstructor
public class MileageOutboxCleaner {

    private final MileageOutboxRepository mileageOutboxRepository;

    // 매일 새벽 3시에 실행 last_attempt_at 기준으로 7일 이전 데이터는 삭제 처리 된다.
    @Scheduled(cron = "0 0 3 * * *")
    public void cleanOldOutboxLogs() {
        LocalDateTime threshold = LocalDateTime.now().minusDays(7);

        int deletedCount = mileageOutboxRepository
                .deleteByStatusAndLastAttemptAtBefore(MileageOutboxStatus.SUCCESS, threshold);

        log.info("[Outbox Cleanup] 삭제된 성공 로그 수: {}", deletedCount);
    }
}