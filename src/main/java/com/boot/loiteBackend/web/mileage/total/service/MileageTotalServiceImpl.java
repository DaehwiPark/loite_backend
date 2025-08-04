package com.boot.loiteBackend.web.mileage.total.service;

import com.boot.loiteBackend.global.error.exception.CustomException;
import com.boot.loiteBackend.web.mileage.total.dto.MileageTotalDto;
import com.boot.loiteBackend.web.mileage.total.entity.MileageTotalEntity;
import com.boot.loiteBackend.web.mileage.total.error.MileageTotalErrorCode;
import com.boot.loiteBackend.web.mileage.total.respository.MileageTotalRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class MileageTotalServiceImpl implements MileageTotalService {

    private final MileageTotalRepository mileageTotalRepository;

    @Override
    @Transactional(readOnly = true)
    public MileageTotalDto getMileageTotal(Long userId) {
        MileageTotalEntity entity = mileageTotalRepository.findById(userId)
                .orElseThrow(() -> new CustomException(MileageTotalErrorCode.NOT_FOUND_MILEAGE_TOTAL));

        return MileageTotalDto.builder()
                .userId(entity.getUserId())
                .mileageTotalAmount(entity.getMileageTotalAmount())
                .mileageTotalEarned(entity.getMileageTotalEarned())
                .mileageTotalUsed(entity.getMileageTotalUsed())
                .mileageTotalExpired(entity.getMileageTotalExpired())
                .build();
    }


    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void increaseMileage(Long userId, int point) {
        try {
            // 1. 기존 레코드 업데이트 시도 (원자적 연산)
            int updatedRows = mileageTotalRepository.increaseMileageAtomic(userId, point);

            // 2. 업데이트된 행이 없으면 신규 사용자이므로 레코드 생성
            if (updatedRows == 0) {
                try {
                    mileageTotalRepository.insertInitialMileage(userId, point);
                    log.info("신규 사용자 마일리지 레코드 생성: userId={}, point={}", userId, point);
                } catch (DataIntegrityViolationException e) {
                    // 동시성으로 인해 다른 트랜잭션이 이미 생성한 경우, 다시 업데이트 시도
                    log.warn("동시 생성 감지, 업데이트 재시도: userId={}", userId);
                    mileageTotalRepository.increaseMileageAtomic(userId, point);
                }
            }

            log.info("마일리지 증가 완료: userId={}, point={}", userId, point);

        } catch (Exception e) {
            log.error("마일리지 증가 실패: userId={}, point={}", userId, point, e);
            throw new RuntimeException("마일리지 증가 처리 중 오류가 발생했습니다.", e);
        }
    }
}