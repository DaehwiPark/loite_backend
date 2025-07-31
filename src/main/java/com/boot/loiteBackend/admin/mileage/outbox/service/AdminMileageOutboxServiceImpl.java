package com.boot.loiteBackend.admin.mileage.outbox.service;

import com.boot.loiteBackend.domain.mileage.outbok.processor.MileageOutboxProcessor;
import com.boot.loiteBackend.domain.mileage.outbok.repository.MileageOutboxRepository;
import com.boot.loiteBackend.web.mileage.outbox.entity.MileageOutboxEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class AdminMileageOutboxServiceImpl implements AdminMileageOutboxService {

    private final MileageOutboxRepository mileageOutboxRepository;
    private final MileageOutboxProcessor mileageOutboxProcessor;

    @Override
    public void retryOutboxEvent(Long id) {
        MileageOutboxEntity event = mileageOutboxRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "이벤트를 찾을 수 없습니다."));

        mileageOutboxProcessor.processEvent(event);
    }
}