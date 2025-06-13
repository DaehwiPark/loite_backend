package com.boot.loiteMsBack.product.brand.service;

import com.boot.loiteMsBack.product.brand.Mapper.ProductBrandMapper;
import com.boot.loiteMsBack.product.brand.dto.ProductBrandRequestDto;
import com.boot.loiteMsBack.product.brand.dto.ProductBrandResponseDto;
import com.boot.loiteMsBack.product.brand.entity.ProductBrandEntity;
import com.boot.loiteMsBack.product.brand.repository.ProductBrandRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
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
        return productBrandRepository.findAll().stream().map(productBrandMapper::toResponseDto).toList();
    }
}
