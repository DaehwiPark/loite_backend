package com.boot.loiteBackend.web.home.mgazineBanner.service;

import com.boot.loiteBackend.admin.product.product.enums.ImageType;
import com.boot.loiteBackend.global.error.exception.CustomException;

import com.boot.loiteBackend.web.home.mgazineBanner.dto.HomeMagazineBannerDto;
import com.boot.loiteBackend.web.home.mgazineBanner.error.HomeMagazineBannerErrorCode;
import com.boot.loiteBackend.web.home.mgazineBanner.repository.HomeMagazineBannerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class HomeMagazineBannerServiceImpl implements HomeMagazineBannerService {

    private final HomeMagazineBannerRepository homeMagazineBannerRepository;

    @Override
    public List<HomeMagazineBannerDto> getAllBanners() {
        List<HomeMagazineBannerDto> list =
                homeMagazineBannerRepository.findActiveMagazineBannersAsDto(ImageType.THUMBNAIL);
        if (list == null || list.isEmpty()) {
            throw new CustomException(HomeMagazineBannerErrorCode.NOT_FOUND);
        }
        return list;
    }
}