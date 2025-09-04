package com.boot.loiteBackend.admin.product.additional.service;

import com.boot.loiteBackend.admin.product.additional.dto.AdminAdditionalRequestDto;
import com.boot.loiteBackend.admin.product.additional.dto.AdminAdditionalResponseDto;
import com.boot.loiteBackend.admin.product.additional.entity.AdminAdditionalEntity;
import com.boot.loiteBackend.admin.product.additional.repository.AdminAdditionalRepository;
import com.boot.loiteBackend.admin.product.product.entity.AdminProductEntity;
import com.boot.loiteBackend.admin.product.product.repository.AdminProductRepository;
import com.boot.loiteBackend.common.file.FileService;
import com.boot.loiteBackend.common.file.FileUploadResult;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class AdminAddtionalServiceImpl implements AdminAdditionalService {

    private final AdminProductRepository adminProductRepository;
    private final AdminAdditionalRepository adminAdditionalRepository;
    private final FileService fileService;

    @Override
    public AdminAdditionalResponseDto createAdditional(AdminAdditionalRequestDto requestDto, MultipartFile imageFile) {
        AdminAdditionalEntity additional = new AdminAdditionalEntity();

        if (imageFile != null && !imageFile.isEmpty()) {
            FileUploadResult result = fileService.save(imageFile, "additional");
            if (result != null) {
                additional.setAdditionalImageUrl(result.getUrlPath());
            }
        }

        additional.setAdditionalName(requestDto.getAdditionalName());
        additional.setAdditionalStock(requestDto.getAdditionalStock());
        additional.setAdditionalPrice(requestDto.getAdditionalPrice());
        additional.setActiveYn(requestDto.getActiveYn() ? "Y" : "N");
        additional.setSoldOutYn(requestDto.getAdditionalStock() <= 0 ? "Y" : "N");


        AdminAdditionalEntity saved = adminAdditionalRepository.save(additional);

        return AdminAdditionalResponseDto.builder()
                .additionalId(saved.getAdditionalId())
                .additionalName(saved.getAdditionalName())
                .additionalStock(saved.getAdditionalStock())
                .additionalPrice(saved.getAdditionalPrice())
                .additionalImageUrl(saved.getAdditionalImageUrl())
                .activeYn("Y".equals(saved.getActiveYn()))
                .soldOutYn("Y".equals(saved.getSoldOutYn()))
                .createdAt(saved.getCreatedAt())
                .updatedAt(saved.getUpdatedAt())
                .build();
    }


    @Override
    public AdminAdditionalResponseDto updateAdditional(Long additionalId, AdminAdditionalRequestDto requestDto, MultipartFile imageFile) {
        AdminAdditionalEntity additional = adminAdditionalRepository.findById(additionalId)
                .orElseThrow(() -> new RuntimeException("추가구성품을 찾을 수 없습니다. ID=" + additionalId));

        if (imageFile != null && !imageFile.isEmpty()) {
            FileUploadResult result = fileService.save(imageFile, "additional");
            if (result != null) {
                additional.setAdditionalImageUrl(result.getUrlPath());
            }
        }

        additional.setAdditionalName(requestDto.getAdditionalName());
        additional.setAdditionalStock(requestDto.getAdditionalStock());
        additional.setAdditionalPrice(requestDto.getAdditionalPrice());
        additional.setActiveYn(requestDto.getActiveYn() ? "Y" : "N");
        additional.setSoldOutYn(requestDto.getAdditionalStock() <= 0 ? "Y" : "N");

        AdminAdditionalEntity saved = adminAdditionalRepository.save(additional);

        return AdminAdditionalResponseDto.builder()
                .additionalId(saved.getAdditionalId())
                .additionalName(saved.getAdditionalName())
                .additionalStock(saved.getAdditionalStock())
                .additionalPrice(saved.getAdditionalPrice())
                .additionalImageUrl(saved.getAdditionalImageUrl())
                .activeYn("Y".equals(saved.getActiveYn()))
                .soldOutYn("Y".equals(saved.getSoldOutYn()))
                .createdAt(saved.getCreatedAt())
                .updatedAt(saved.getUpdatedAt())
                .build();
    }


    @Override
    public void deleteAdditional(Long additionalId) {
        AdminAdditionalEntity additional = adminAdditionalRepository.findById(additionalId)
                .orElseThrow(() -> new RuntimeException("추가구성품을 찾을 수 없습니다. ID=" + additionalId));
        additional.setDeleteYn("Y");

        adminAdditionalRepository.save(additional);
    }

    @Override
    public AdminAdditionalResponseDto getAdditional(Long additionalId) {
        AdminAdditionalEntity entity = adminAdditionalRepository.findByAdditionalIdAndDeleteYn(additionalId, "N")
                .orElseThrow(() -> new RuntimeException("추가구성품을 찾을 수 없습니다. ID=" + additionalId));

        return AdminAdditionalResponseDto.builder()
                .additionalId(entity.getAdditionalId())
                .additionalName(entity.getAdditionalName())
                .additionalStock(entity.getAdditionalStock())
                .additionalPrice(entity.getAdditionalPrice())
                .additionalImageUrl(entity.getAdditionalImageUrl())
                .activeYn("Y".equals(entity.getActiveYn()))
                .soldOutYn("Y".equals(entity.getSoldOutYn()))
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }

    @Override
    public Page<AdminAdditionalResponseDto> getAllAdditionals(Pageable pageable) {
        Page<AdminAdditionalEntity> page = adminAdditionalRepository.findByDeleteYn("N", pageable);

        return page.map(entity -> AdminAdditionalResponseDto.builder()
                .additionalId(entity.getAdditionalId())
                .additionalName(entity.getAdditionalName())
                .additionalStock(entity.getAdditionalStock())
                .additionalPrice(entity.getAdditionalPrice())
                .additionalImageUrl(entity.getAdditionalImageUrl())
                .activeYn("Y".equals(entity.getActiveYn()))
                .soldOutYn("Y".equals(entity.getSoldOutYn()))
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build()
        );
    }


}
