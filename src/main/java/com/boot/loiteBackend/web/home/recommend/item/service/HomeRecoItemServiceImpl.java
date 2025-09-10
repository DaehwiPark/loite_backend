package com.boot.loiteBackend.web.home.recommend.item.service;

import com.boot.loiteBackend.global.error.exception.CustomException;
import com.boot.loiteBackend.web.home.recommend.item.dto.HomeRecoItemResponseDto;
import com.boot.loiteBackend.web.home.recommend.item.error.HomeRecoItemErrorCode;
import com.boot.loiteBackend.web.home.recommend.item.repository.HomeRecoItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class HomeRecoItemServiceImpl implements HomeRecoItemService {

    private final HomeRecoItemRepository heroSectionRepository;

    @Override
    public List<HomeRecoItemResponseDto> findItemsBySection(Long sectionId) {
        if (sectionId == null || sectionId <= 0) {
            throw new IllegalArgumentException("sectionId는 양수여야 합니다.");
        }

        if (!heroSectionRepository.existsById(sectionId)) {
            throw new CustomException(HomeRecoItemErrorCode.SECTION_NOT_FOUND);
        }

        return heroSectionRepository.findActiveBySectionIdAsDto(sectionId);
    }
}
