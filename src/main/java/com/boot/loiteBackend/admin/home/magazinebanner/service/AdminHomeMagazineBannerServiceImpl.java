package com.boot.loiteBackend.admin.home.magazinebanner.service;

import com.boot.loiteBackend.admin.home.magazinebanner.dto.AdminHomeMagazineBannerDto;
import com.boot.loiteBackend.admin.home.magazinebanner.error.AdminHomeMagazineBannerErrorCode;
import com.boot.loiteBackend.admin.home.magazinebanner.repository.AdminHomeMagazineBannerRepository;
import com.boot.loiteBackend.common.file.FileService;
import com.boot.loiteBackend.common.file.FileUploadResult;
import com.boot.loiteBackend.domain.home.magazinebanner.entity.HomeMagazineBannerEntity;
import com.boot.loiteBackend.global.error.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class AdminHomeMagazineBannerServiceImpl implements AdminHomeMagazineBannerService {

    private final AdminHomeMagazineBannerRepository repository;
    private final FileService fileService;
    private static final String UPLOAD_CATEGORY = "etc";

    @Override
    public AdminHomeMagazineBannerDto.Response create(AdminHomeMagazineBannerDto.Request request,
                                                      MultipartFile videoFile,
                                                      MultipartFile thumbnailFile) {

        FileUploadResult videoResult = null;
        FileUploadResult thumbResult = null;

        if (videoFile != null && !videoFile.isEmpty()) {
            videoResult = fileService.save(videoFile, UPLOAD_CATEGORY);
            request.setVideoUrl(videoResult.getUrlPath());
        }
        if (thumbnailFile != null && !thumbnailFile.isEmpty()) {
            thumbResult = fileService.save(thumbnailFile, UPLOAD_CATEGORY);
            request.setVideoThumbnailUrl(thumbResult.getUrlPath());
        }

        HomeMagazineBannerEntity entity = HomeMagazineBannerEntity.builder()
                .productId(request.getProductId())

                .videoName(videoResult != null ? videoResult.getFileName() : null)
                .videoUrl(request.getVideoUrl())
                .videoPath(videoResult != null ? videoResult.getPhysicalPath() : null)
                .videoSize(videoResult != null ? videoResult.getSize() : null)
                .videoType(videoResult != null ? videoResult.getContentType() : null)

                .thumbnailName(thumbResult != null ? thumbResult.getFileName() : null)
                .videoThumbnailUrl(request.getVideoThumbnailUrl())
                .thumbnailPath(thumbResult != null ? thumbResult.getPhysicalPath() : null)
                .thumbnailSize(thumbResult != null ? thumbResult.getSize() : null)
                .thumbnailType(thumbResult != null ? thumbResult.getContentType() : null)

                .title(request.getTitle())
                .subtitle(request.getSubtitle())
                .buttonText(request.getButtonText())
                .buttonUrl(request.getButtonUrl())
                .displayYn(request.getDisplayYn())
                .sortOrder(request.getSortOrder())
                .build();

        HomeMagazineBannerEntity saved = repository.save(entity);
        return AdminHomeMagazineBannerDto.Response.fromEntity(saved);
    }

    @Override
    public AdminHomeMagazineBannerDto.Response update(Long id,
                                                      AdminHomeMagazineBannerDto.Request request,
                                                      MultipartFile videoFile,
                                                      MultipartFile thumbnailFile) {
        HomeMagazineBannerEntity banner = repository.findById(id)
                .orElseThrow(() -> new CustomException(AdminHomeMagazineBannerErrorCode.NOT_FOUND));

        // 영상 교체
        if (videoFile != null && !videoFile.isEmpty()) {
            fileService.deleteQuietly(banner.getVideoPath());

            FileUploadResult result = fileService.save(videoFile, UPLOAD_CATEGORY);
            banner.setVideoName(result.getFileName());
            banner.setVideoUrl(result.getUrlPath());
            banner.setVideoPath(result.getPhysicalPath());
            banner.setVideoSize(result.getSize());
            banner.setVideoType(result.getContentType());
        }

        // 썸네일 교체
        if (thumbnailFile != null && !thumbnailFile.isEmpty()) {
            fileService.deleteQuietly(banner.getThumbnailPath());

            FileUploadResult result = fileService.save(thumbnailFile, UPLOAD_CATEGORY);
            banner.setThumbnailName(result.getFileName());
            banner.setVideoThumbnailUrl(result.getUrlPath());
            banner.setThumbnailPath(result.getPhysicalPath());
            banner.setThumbnailSize(result.getSize());
            banner.setThumbnailType(result.getContentType());
        }

        AdminHomeMagazineBannerDto.Request.apply(banner, request);

        HomeMagazineBannerEntity updated = repository.save(banner);
        return AdminHomeMagazineBannerDto.Response.fromEntity(updated);
    }

    @Override
    public void delete(Long id) {
        HomeMagazineBannerEntity banner = repository.findById(id)
                .orElseThrow(() -> new CustomException(AdminHomeMagazineBannerErrorCode.NOT_FOUND));
        fileService.deleteQuietly(banner.getVideoPath());
        fileService.deleteQuietly(banner.getThumbnailPath());
        repository.delete(banner);
    }

    @Override
    public AdminHomeMagazineBannerDto.Response getOne(Long id) {
        HomeMagazineBannerEntity entity = repository.findById(id)
                .orElseThrow(() -> new CustomException(AdminHomeMagazineBannerErrorCode.NOT_FOUND));
        return AdminHomeMagazineBannerDto.Response.fromEntity(entity);
    }

    @Override
    public Page<AdminHomeMagazineBannerDto.Response> getList(Pageable pageable) {
        return repository.findAll(pageable).map(AdminHomeMagazineBannerDto.Response::fromEntity);
    }
}