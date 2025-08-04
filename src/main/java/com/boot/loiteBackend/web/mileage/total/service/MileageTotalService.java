package com.boot.loiteBackend.web.mileage.total.service;

import com.boot.loiteBackend.web.mileage.total.dto.MileageTotalDto;

public interface MileageTotalService {

    MileageTotalDto getMileageTotal(Long userId);

    void increaseMileage(Long userId, int point);
}
