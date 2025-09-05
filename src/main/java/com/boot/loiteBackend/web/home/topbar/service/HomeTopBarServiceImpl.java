package com.boot.loiteBackend.web.home.topbar.service;

import com.boot.loiteBackend.domain.home.topbar.mapper.HomeTopBarMapper;
import com.boot.loiteBackend.global.error.exception.CustomException;
import com.boot.loiteBackend.web.home.topbar.dto.HomeTopBarResponseDto;
import com.boot.loiteBackend.web.home.topbar.error.HomeTopBarErrorCode;
import com.boot.loiteBackend.web.home.topbar.repository.HomeTopBarWebRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class HomeTopBarServiceImpl implements HomeTopBarService {

    private final HomeTopBarWebRepository repository;
    private final HomeTopBarMapper mapper;

    @Override
    public HomeTopBarResponseDto getActiveTopBar() {
        return repository.findDefaultVisibleOne()
                .map(mapper::toWebResponse)
                .orElseThrow(() -> new CustomException(HomeTopBarErrorCode.ACTIVE_NOT_FOUND));
    }
}
