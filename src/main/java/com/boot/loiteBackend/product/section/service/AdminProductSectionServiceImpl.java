package com.boot.loiteBackend.product.section.service;

import com.boot.loiteBackend.product.product.entity.AdminProductEntity;
import com.boot.loiteBackend.product.section.dto.AdminProductSectionRequestDto;
import com.boot.loiteBackend.product.section.entity.AdminProductSectionEntity;
import com.boot.loiteBackend.product.section.mapper.AdminProductSectionMapper;
import com.boot.loiteBackend.product.section.repository.AdminProductSectionRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class AdminProductSectionServiceImpl implements AdminProductSectionService {

    private final AdminProductSectionRepository adminProductSectionRepository;
    private final AdminProductSectionMapper adminProductSectionMapper;

    @Override
    public void saveSections(AdminProductEntity product, List<AdminProductSectionRequestDto> dto) {
        if (dto == null || dto.isEmpty()) return;

        List<AdminProductSectionEntity> entities = dto.stream()
                .map(d -> adminProductSectionMapper.toEntity(d, product))
                .toList();

        adminProductSectionRepository.saveAll(entities);
    }

    @Override
    public void updateSections(AdminProductEntity product, List<AdminProductSectionRequestDto> dto) {
        adminProductSectionRepository.deleteByProduct(product);

        saveSections(product, dto);
    }
}
