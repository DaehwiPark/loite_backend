package com.boot.loiteBackend.domain.mileage.outbok.repository;

import com.boot.loiteBackend.web.mileage.outbox.entity.MileageOutboxEntity;
import com.boot.loiteBackend.web.mileage.outbox.model.MileageOutboxStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;

import java.time.LocalDateTime;
import java.util.List;

public interface MileageOutboxRepository extends JpaRepository<MileageOutboxEntity, Long> {

    // 상태별 단건 처리용 (PENDING만)
    List<MileageOutboxEntity> findTop10ByStatusOrderByCreatedAtAsc(MileageOutboxStatus status);

    // 실패 이벤트 조회용 (관리자 페이지)
    Page<MileageOutboxEntity> findByStatus(MileageOutboxStatus status, PageRequest pageable);

    // 재시도 대상 이벤트 조회용 (스케줄러)
    List<MileageOutboxEntity> findTop10ByStatusInAndRetryCountLessThanOrderByLastAttemptAtAsc(
            List<MileageOutboxStatus> statuses, int maxRetryCount
    );

    // outbok 삭제 매일 새벽 3시 (스케줄러)
    @Modifying
    @org.springframework.transaction.annotation.Transactional
    int deleteByStatusAndLastAttemptAtBefore(MileageOutboxStatus status, LocalDateTime beforeTime);
}