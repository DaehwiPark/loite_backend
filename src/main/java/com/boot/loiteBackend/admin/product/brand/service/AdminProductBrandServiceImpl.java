package com.boot.loiteBackend.admin.product.brand.service;

import com.boot.loiteBackend.admin.product.brand.Mapper.AdminProductBrandMapper;
import com.boot.loiteBackend.admin.product.brand.dto.AdminProductBrandRequestDto;
import com.boot.loiteBackend.admin.product.brand.dto.AdminProductBrandResponseDto;
import com.boot.loiteBackend.admin.product.brand.entity.AdminProductBrandEntity;
import com.boot.loiteBackend.admin.product.brand.repository.AdminProductBrandRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class AdminProductBrandServiceImpl implements AdminProductBrandService {
    private final AdminProductBrandRepository adminProductBrandRepository;
    private final AdminProductBrandMapper adminProductBrandMapper;

    @Override
    public Long saveBrand(AdminProductBrandRequestDto dto){
        AdminProductBrandEntity productBrand = adminProductBrandMapper.toEntity(dto);
        return adminProductBrandRepository.save(productBrand).getBrandId();
    }

    @Override
    public List<AdminProductBrandResponseDto> getAllBrands() {
        return adminProductBrandRepository.findAllByDeleteYn("N").stream()
                .map(adminProductBrandMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public void updateBrand(Long brandId, AdminProductBrandRequestDto dto) {
        AdminProductBrandEntity brand = adminProductBrandRepository.findById(brandId)
                .orElseThrow(() -> new EntityNotFoundException("해당 브랜드가 존재하지 않습니다."));

        brand.setBrandName(dto.getBrandName());
        brand.setBrandOrigin(dto.getBrandOrigin());
        brand.setBrandLogoUrl(dto.getBrandLogoUrl());
        brand.setBrandDescription(dto.getBrandDescription());
        brand.setActiveYn(dto.getActiveYn());
        brand.setUpdatedAt(LocalDateTime.now());
    }

    @Override
    public void deleteBrand(Long brandId) {
        AdminProductBrandEntity brand = adminProductBrandRepository.findById(brandId)
                .orElseThrow(()-> new EntityNotFoundException("해당 브랜드가 존재하지 않습니다."));

        brand.setDeleteYn("Y");
        brand.setUpdatedAt(LocalDateTime.now());
    }
}
