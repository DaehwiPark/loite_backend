package com.boot.loiteBackend.admin.mileage.outbox.controller;

import com.boot.loiteBackend.domain.mileage.outbok.processor.MileageOutboxProcessor;
import com.boot.loiteBackend.domain.mileage.outbok.repository.MileageOutboxRepository;
import com.boot.loiteBackend.web.mileage.outbox.entity.MileageOutboxEntity;
import com.boot.loiteBackend.web.mileage.outbox.model.MileageOutboxStatus;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/admin/mileage/outbox")
@RequiredArgsConstructor
@Tag(name = "마일리지 Outbox 관리 API", description = "Outbox 이벤트 조회 및 재처리 기능을 제공합니다.")
public class AdminMileageOutboxController {

    private final MileageOutboxProcessor mileageOutboxProcessor;
    private final MileageOutboxRepository mileageOutboxRepository;

    @GetMapping("/failed")
    @Operation(summary = "실패한 Outbox 이벤트 조회", description = "FAILED 상태의 마일리지 Outbox 이벤트를 페이지별로 조회합니다.")
    public ResponseEntity<?> getFailedOutboxEvents(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Page<MileageOutboxEntity> failedEvents = mileageOutboxRepository.findByStatus(
                MileageOutboxStatus.FAILED,
                PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "lastAttemptAt"))
        );

        return ResponseEntity.ok(failedEvents);
    }

    @PostMapping("/retry/{id}")
    @Operation(summary = "Outbox 이벤트 재처리", description = "특정 ID의 마일리지 Outbox 이벤트를 수동으로 재처리합니다.")
    public ResponseEntity<?> retryOutbox(@PathVariable Long id) {
        MileageOutboxEntity event = mileageOutboxRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "이벤트를 찾을 수 없습니다."));

        mileageOutboxProcessor.processEvent(event); // REQUIRES_NEW로 트랜잭션 처리
        return ResponseEntity.ok("이벤트 재처리가 완료되었습니다.");
    }
}