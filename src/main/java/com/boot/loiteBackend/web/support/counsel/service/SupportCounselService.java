package com.boot.loiteBackend.web.support.counsel.service;

import com.boot.loiteBackend.web.support.counsel.dto.SupportCounselRequestDto;
import com.boot.loiteBackend.web.support.counsel.dto.SupportCounselResponseDto;

import java.util.List;

public interface SupportCounselService {
    Long createCounsel(Long userId, SupportCounselRequestDto dto);
    List<SupportCounselResponseDto> getMyCounsels(Long userId);
    SupportCounselResponseDto getCounselDetail(Long counselId, Long userId, String inputPassword);
    boolean verifyPassword(Long counselId, String inputPassword);
}