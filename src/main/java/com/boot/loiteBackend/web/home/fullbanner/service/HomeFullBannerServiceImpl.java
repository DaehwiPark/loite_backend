package com.boot.loiteBackend.web.home.fullbanner.service;

import com.boot.loiteBackend.domain.home.fullbanner.entity.HomeFullBannerEntity;
import com.boot.loiteBackend.global.error.exception.CustomException;
import com.boot.loiteBackend.web.home.fullbanner.dto.HomeFullBannerResponseDto;
import com.boot.loiteBackend.web.home.fullbanner.error.HomeFullBannerErrorCode;
import com.boot.loiteBackend.web.home.fullbanner.repository.HomeFullBannerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class HomeFullBannerServiceImpl implements HomeFullBannerService {

    private final HomeFullBannerRepository repository;

    /** sortOrder ASC → startAt DESC → id DESC */
    private Sort defaultSort() {
        return Sort.by(
                Sort.Order.asc("sortOrder"),
                Sort.Order.desc("startAt"),
                Sort.Order.desc("id")
        );
    }

    @Override
    public HomeFullBannerResponseDto getActiveDefault() {
        LocalDateTime now = LocalDateTime.now();
        var pageableTop1 = PageRequest.of(0, 1, defaultSort());
        List<HomeFullBannerEntity> list = repository.findActiveDefaultForNow(now, pageableTop1);

        if (list.isEmpty()) {
            throw new CustomException(HomeFullBannerErrorCode.ACTIVE_NOT_FOUND);
        }
        return toDto(list.get(0));
    }

    /** 간단 매퍼 (MapStruct 없이) */
    private HomeFullBannerResponseDto toDto(HomeFullBannerEntity e) {
        return HomeFullBannerResponseDto.builder()
                .id(e.getId())
                .title(e.getTitle())
                .subtitle(e.getSubtitle())
                .titleColor(e.getTitleColor())
                .subtitleColor(e.getSubtitleColor())
                .bgImageUrl(e.getBgImageUrl())
                .buttonText(e.getButtonText())
                .buttonLinkUrl(e.getButtonLinkUrl())
                .buttonLinkTarget(e.getButtonLinkTarget())
                .buttonTextColor(e.getButtonTextColor())
                .buttonBgColor(e.getButtonBgColor())
                .buttonBorderColor(e.getButtonBorderColor())
                .startAt(e.getStartAt())
                .endAt(e.getEndAt())
                .sortOrder(e.getSortOrder())
                .build();
    }
}
