package com.boot.loiteBackend.admin.support.faq.category.service;

import com.boot.loiteBackend.admin.support.faq.category.dto.AdminSupportFaqMediumCategoryDto;
import com.boot.loiteBackend.admin.support.faq.category.dto.AdminSupportFaqMediumCategoryRequestDto;
import com.boot.loiteBackend.admin.support.faq.category.error.AdminFaqCategoryErrorCode;
import com.boot.loiteBackend.admin.support.faq.category.repository.AdminSupportFaqMajorCategoryRepository;
import com.boot.loiteBackend.admin.support.faq.category.repository.AdminSupportFaqMediumCategoryRepository;
import com.boot.loiteBackend.common.file.FileService;
import com.boot.loiteBackend.common.file.FileUploadResult;
import com.boot.loiteBackend.domain.support.faq.category.entity.SupportFaqMajorCategoryEntity;
import com.boot.loiteBackend.domain.support.faq.category.entity.SupportFaqMediumCategoryEntity;
import com.boot.loiteBackend.global.error.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminSupportFaqMediumCategoryServiceImpl implements AdminSupportFaqMediumCategoryService {

    private final AdminSupportFaqMediumCategoryRepository mediumCategoryRepository;
    private final AdminSupportFaqMajorCategoryRepository majorCategoryRepository;
    private final FileService fileService;

    @Override
    @Transactional
    public AdminSupportFaqMediumCategoryDto createCategory(AdminSupportFaqMediumCategoryRequestDto request, MultipartFile faqImage) {
        if (faqImage == null || faqImage.isEmpty()) {
            throw new CustomException(AdminFaqCategoryErrorCode.INVALID_THUMBNAIL);
        }
        FileUploadResult uploadResult;
        try {
            String uploadCategory = "etc";
            uploadResult = fileService.save(faqImage, uploadCategory);
        } catch (Exception e) {
            throw new CustomException(AdminFaqCategoryErrorCode.THUMBNAIL_UPLOAD_FAILED);
        }

        SupportFaqMajorCategoryEntity majorCategory = majorCategoryRepository.findById(request.getFaqMajorCategoryId())
                .orElseThrow(() -> new CustomException(AdminFaqCategoryErrorCode.MAJOR_CATEGORY_NOT_FOUND));

        SupportFaqMediumCategoryEntity entity = SupportFaqMediumCategoryEntity.builder()
                .faqMediumCategoryName(request.getFaqMediumCategoryName())
                .faqMediumCategoryOrder(request.getFaqMediumCategoryOrder())
                .faqMajorCategory(majorCategory)
                .faqImageName(request.getFaqImageName())
                .faqImagePath(uploadResult.getPhysicalPath())
                .faqImageUrl(uploadResult.getUrlPath())
                .build();

        mediumCategoryRepository.save(entity);
        return new AdminSupportFaqMediumCategoryDto(entity);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AdminSupportFaqMediumCategoryDto> getAllCategories() {
        return mediumCategoryRepository.findAll().stream()
                .map(AdminSupportFaqMediumCategoryDto::new)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public AdminSupportFaqMediumCategoryDto getCategoryById(Long id) {
        SupportFaqMediumCategoryEntity entity = mediumCategoryRepository.findById(id)
                .orElseThrow(() -> new CustomException(AdminFaqCategoryErrorCode.NOT_FOUND));
        return new AdminSupportFaqMediumCategoryDto(entity);
    }

    @Override
    @Transactional
    public AdminSupportFaqMediumCategoryDto updateCategory(Long id, AdminSupportFaqMediumCategoryRequestDto request, MultipartFile faqImage) {
        // 중분류 조회
        SupportFaqMediumCategoryEntity entity = mediumCategoryRepository.findById(id)
                .orElseThrow(() -> new CustomException(AdminFaqCategoryErrorCode.NOT_FOUND));

        // 대분류 존재 여부 확인
        SupportFaqMajorCategoryEntity majorCategory = majorCategoryRepository.findById(request.getFaqMajorCategoryId())
                .orElseThrow(() -> new CustomException(AdminFaqCategoryErrorCode.MAJOR_CATEGORY_NOT_FOUND));

        // 이미지가 넘어온 경우 새로 업로드
        if (faqImage != null && !faqImage.isEmpty()) {
            try {
                FileUploadResult result = fileService.save(faqImage, "etc");

                // 새로운 이미지 정보로 업데이트
                entity.setFaqImageName(request.getFaqImageName());
                entity.setFaqImageUrl(result.getUrlPath());
                entity.setFaqImagePath(result.getPhysicalPath());
            } catch (Exception e) {
                throw new CustomException(AdminFaqCategoryErrorCode.THUMBNAIL_UPLOAD_FAILED);
            }
        }

        // 나머지 필드 업데이트
        entity.setFaqMediumCategoryName(request.getFaqMediumCategoryName());
        entity.setFaqMediumCategoryOrder(request.getFaqMediumCategoryOrder());
        entity.setFaqMajorCategory(majorCategory);

        return new AdminSupportFaqMediumCategoryDto(entity);
    }

    @Override
    @Transactional
    public void deleteCategory(Long id) {
        if (!mediumCategoryRepository.existsById(id)) {
            throw new CustomException(AdminFaqCategoryErrorCode.DELETE_FAILED);
        }
        mediumCategoryRepository.deleteById(id);
    }


    @Override
    public List<AdminSupportFaqMediumCategoryDto> getMediumsByMajorCategoryId(Long majorId) {
        List<SupportFaqMediumCategoryEntity> entities =
                mediumCategoryRepository.findByFaqMajorCategory_FaqMajorCategoryId(majorId);

        return entities.stream()
                .map(entity -> AdminSupportFaqMediumCategoryDto.builder()
                        .faqMediumCategoryId(entity.getFaqMediumCategoryId())
                        .faqMediumCategoryName(entity.getFaqMediumCategoryName())
                        .faqMediumCategoryOrder(entity.getFaqMediumCategoryOrder())
                        .faqMajorCategoryId(entity.getFaqMajorCategory().getFaqMajorCategoryId())
                        .faqMajorCategoryName(entity.getFaqMajorCategory().getFaqMajorCategoryName())
                        .faqImageName(entity.getFaqImageName())
                        .faqImageUrl(entity.getFaqImageUrl())
                        .faqImagePath(entity.getFaqImagePath())
                        .build())
                .collect(Collectors.toList());
    }

}
