package com.boot.loiteBackend.web.home.best.service;

import com.boot.loiteBackend.web.home.best.dto.HomeBestItemResponseDto;
import com.boot.loiteBackend.web.home.best.error.HomeBestItemErrorCode;
import com.boot.loiteBackend.global.error.exception.CustomException;
import com.boot.loiteBackend.web.home.best.repository.HomeBestItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class HomeBestItemServiceImpl implements HomeBestItemService {

    private final HomeBestItemRepository homeBestItemRepository;

    @Override
    public List<HomeBestItemResponseDto> getBestItemList() {
        List<HomeBestItemResponseDto> list = homeBestItemRepository.findActiveBestItemsAsDto();
        if (list == null || list.isEmpty()) {
            throw new CustomException(HomeBestItemErrorCode.NOT_FOUND);
        }
        return list;
    }
}
