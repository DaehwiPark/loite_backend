package com.boot.loiteBackend.web.mileage.history.service;

import com.boot.loiteBackend.web.mileage.history.dto.MileageHistoryDto;

import java.util.List;

public interface MileageHistoryService {

    List<MileageHistoryDto> getHistories(Long userId);

    void earnSignupMileage(Long userId, int point, String reason, Long mileagePolicyId);
}
