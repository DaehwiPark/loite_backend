package com.boot.loiteBackend.product.section.service;

import com.boot.loiteBackend.product.product.entity.ProductEntity;
import com.boot.loiteBackend.product.section.dto.ProductSectionRequestDto;
import com.boot.loiteBackend.product.section.entity.ProductSectionEntity;
import com.boot.loiteBackend.product.section.mapper.ProductSectionMapper;
import com.boot.loiteBackend.product.section.repository.ProductSectionRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ProductSectionServiceImpl implements ProductSectionService {

    private final ProductSectionRepository productSectionRepository;
    private final ProductSectionMapper productSectionMapper;

    @Override
    public void saveSections(ProductEntity product, List<ProductSectionRequestDto> dto) {
        if (dto == null || dto.isEmpty()) return;

        List<ProductSectionEntity> entities = dto.stream()
                .map(d -> productSectionMapper.toEntity(d, product))
                .toList();

        productSectionRepository.saveAll(entities);
    }

    @Override
    public void updateSections(ProductEntity product, List<ProductSectionRequestDto> dto) {
        productSectionRepository.deleteByProduct(product);

        saveSections(product, dto);
    }
}
