package com.boot.loiteBackend.product.category.service;

import com.boot.loiteBackend.product.category.dto.ProductCategoryRequestDto;
import com.boot.loiteBackend.product.category.dto.ProductCategoryResponseDto;
import com.boot.loiteBackend.product.category.entity.ProductCategoryEntity;
import com.boot.loiteBackend.product.category.mapper.ProductCategoryMapper;
import com.boot.loiteBackend.product.category.repository.ProductCategoryRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class ProductCategoryServiceImpl implements ProductCategoryService {

    private final ProductCategoryRepository productCategoryRepository;
    private final ProductCategoryMapper productCategoryMapper;
    private final ModelMapper modelMapper;

    @Override
    public Long saveCategory(ProductCategoryRequestDto dto) {
        if (dto.getCategoryId() == null) {
            // 신규 등록
            ProductCategoryEntity category = productCategoryMapper.toEntity(dto);

            if (dto.getCategoryParentId() != null) {
                ProductCategoryEntity parent = productCategoryRepository.findById(dto.getCategoryParentId())
                        .orElseThrow(() -> new IllegalArgumentException("상위 카테고리가 존재하지 않습니다."));
                category.setCategoryParentId(parent);
            }

            if (category.getActiveYn() == null) category.setActiveYn("Y");
            if (category.getDeleteYn() == null) category.setDeleteYn("N");
            if (category.getCategoryDepth() == null) {
                category.setCategoryDepth(dto.getCategoryParentId() == null ? 1 : calculateDepth(dto.getCategoryParentId()));
            }

            category.setCreatedAt(LocalDateTime.now());
            category.setUpdatedAt(LocalDateTime.now());

            ProductCategoryEntity saved = productCategoryRepository.save(category);
            return saved.getCategoryId();

        } else {
            // 수정
            ProductCategoryEntity category = productCategoryRepository.findById(dto.getCategoryId())
                    .orElseThrow(() -> new EntityNotFoundException("카테고리가 존재하지 않습니다."));

            category.setCategoryName(dto.getCategoryName());
            category.setCategorySortOrder(dto.getCategorySortOrder());
            category.setActiveYn(dto.getActiveYn());
            category.setUpdatedAt(LocalDateTime.now());

            if (dto.getCategoryParentId() != null) {
                ProductCategoryEntity parent = productCategoryRepository.findById(dto.getCategoryParentId())
                        .orElseThrow(() -> new IllegalArgumentException("상위 카테고리가 존재하지 않습니다."));
                category.setCategoryParentId(parent);
                category.setCategoryDepth(calculateDepth(dto.getCategoryParentId()));
            } else {
                category.setCategoryParentId(null);
                category.setCategoryDepth(1);
            }

            return category.getCategoryId();
        }
    }

    private int calculateDepth(Long categoryParentId) {
        ProductCategoryEntity categoryParent = productCategoryRepository.findById(categoryParentId)
                .orElseThrow(() -> new IllegalArgumentException("상위 카테고리가 존재하지 않습니다."));

        int parentDepth = categoryParent.getCategoryDepth();
        if (parentDepth >= 3) {
            throw new IllegalArgumentException("카테고리는 최대 3뎁스까지만 등록 가능합니다.");
        }

        return parentDepth + 1;
    }

    @Override
    public List<ProductCategoryResponseDto> getAllCategory() {
        List<ProductCategoryEntity> categories = productCategoryRepository.findAllByDeleteYn("N"); // ✅ 변경됨

        List<ProductCategoryEntity> topLevel = categories.stream()
                .filter(category -> category.getCategoryParentId() == null)
                .collect(Collectors.toList());

        return topLevel.stream()
                .map(category -> buildTree(category, categories))
                .collect(Collectors.toList());
    }

    @Override
    public void deleteCategory(Long categoryId) {
        ProductCategoryEntity category = productCategoryRepository.findById(categoryId)
                .orElseThrow(() -> new EntityNotFoundException("해당 카테고리가 존재하지 않습니다."));

        category.setDeleteYn("Y");
        category.setUpdatedAt(LocalDateTime.now());
    }

    private ProductCategoryResponseDto buildTree(ProductCategoryEntity parent, List<ProductCategoryEntity> allCategories) {
        ProductCategoryResponseDto dto = modelMapper.map(parent, ProductCategoryResponseDto.class);

        List<ProductCategoryResponseDto> children = allCategories.stream()
                .filter(child -> {
                    ProductCategoryEntity parentRef = child.getCategoryParentId();
                    return parentRef != null && parentRef.getCategoryId().equals(parent.getCategoryId());
                })
                .map(child -> buildTree(child, allCategories))
                .collect(Collectors.toList());

        dto.setChildren(children);
        return dto;
    }
}
