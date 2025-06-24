package com.boot.loiteMsBack.product.brand.service;

import com.boot.loiteMsBack.product.brand.Mapper.ProductBrandMapper;
import com.boot.loiteMsBack.product.brand.dto.ProductBrandRequestDto;
import com.boot.loiteMsBack.product.brand.dto.ProductBrandResponseDto;
import com.boot.loiteMsBack.product.brand.entity.ProductBrandEntity;
import com.boot.loiteMsBack.product.brand.repository.ProductBrandRepository;
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
public class ProductBrandServiceImpl implements ProductBrandService {
    private final ProductBrandRepository productBrandRepository;
    private final ProductBrandMapper productBrandMapper;

    @Override
    public Long saveBrand(ProductBrandRequestDto dto){
        ProductBrandEntity productBrand = productBrandMapper.toEntity(dto);
        return productBrandRepository.save(productBrand).getBrandId();
    }

    @Override
    public List<ProductBrandResponseDto> getAllBrands() {
        return productBrandRepository.findAllByDeleteYn("N").stream()
                .map(productBrandMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public void updateBrand(Long brandId, ProductBrandRequestDto dto) {
        ProductBrandEntity brand = productBrandRepository.findById(brandId)
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
        ProductBrandEntity brand = productBrandRepository.findById(brandId)
                .orElseThrow(()-> new EntityNotFoundException("해당 브랜드가 존재하지 않습니다."));

        brand.setDeleteYn("Y");
        brand.setUpdatedAt(LocalDateTime.now());
    }
}
