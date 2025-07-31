package com.boot.loiteBackend.web.mileage.history.service;

import com.boot.loiteBackend.web.mileage.history.dto.MileageHistoryDto;
import com.boot.loiteBackend.web.mileage.history.entity.MileageHistoryEntity;
import com.boot.loiteBackend.web.mileage.history.model.MileageHistoryType;
import com.boot.loiteBackend.web.mileage.history.respository.MileageHistoryRepository;
import com.boot.loiteBackend.web.mileage.total.respository.MileageTotalRepository;
import com.boot.loiteBackend.web.mileage.total.service.MileageTotalService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class MileageHistoryServiceImpl implements MileageHistoryService {

    private final MileageHistoryRepository mileageHistoryRepository;
    private final MileageTotalRepository mileageTotalRepository;
    private final MileageTotalService mileageTotalService;

    @Override
    public List<MileageHistoryDto> getHistories(Long userId) {
        return mileageHistoryRepository.findByUserId(userId).stream()
                .map(entity -> MileageHistoryDto.builder()
                        .userId(entity.getUserId())
                        .mileageHistoryType(entity.getMileageHistoryType().name())
                        .mileageHistorySource(entity.getMileageHistorySource())
                        .mileageHistoryAmount(entity.getMileageHistoryAmount())
                        .mileageHistoryTotalAmount(entity.getMileageHistoryTotalAmount())
                        .orderId(entity.getOrderId())
                        .mileagePolicyId(entity.getMileagePolicyId())
                        .createdAt(entity.getCreatedAt())
                        .build())
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void earnSignupMileage(Long userId, int point, String reason, Long policyId) {
        MileageHistoryEntity history = MileageHistoryEntity.builder()
                .userId(userId)
                .mileageHistoryAmount(point)
                .mileageHistoryType(MileageHistoryType.EARN)
                .mileageHistorySource(reason)
                .mileagePolicyId(policyId)
                .build();

        mileageHistoryRepository.save(history);
        System.out.println("history 저장 된다.");

        mileageTotalService.increaseMileage(userId, point);
        System.out.println("total 끝");
    }
}

