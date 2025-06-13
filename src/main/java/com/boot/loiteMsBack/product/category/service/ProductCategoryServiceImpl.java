package com.boot.loiteMsBack.product.category.service;

import com.boot.loiteMsBack.product.category.dto.ProductCategoryRequestDto;
import com.boot.loiteMsBack.product.category.dto.ProductCategoryResponseDto;
import com.boot.loiteMsBack.product.category.entity.ProductCategoryEntity;
import com.boot.loiteMsBack.product.category.mapper.ProductCategoryMapper;
import com.boot.loiteMsBack.product.category.repository.ProductCategoryRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.internal.bytebuddy.implementation.bytecode.Throw;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ProductCategoryServiceImpl implements ProductCategoryService  {

    private final ProductCategoryRepository productCategoryRepository;
    private final ProductCategoryMapper productCategoryMapper;
    private final ModelMapper modelMapper;

    @Override
    public Long saveCategory(ProductCategoryRequestDto dto) {
        ProductCategoryEntity category = productCategoryMapper.toEntity(dto);

        if(dto.getCategoryParentId() != null){
            ProductCategoryEntity parent = productCategoryRepository.findById(dto.getCategoryParentId())
                .orElseThrow(()->
                    new IllegalArgumentException("상위 카테고리가 존재하지 않습니다.")
                );
            category.setCategoryParentId(parent);
        }

        if(category.getActiveYn() == null){
            category.setActiveYn("Y");
        }
        if(category.getDeleteYn() == null){
            category.setDeleteYn("N");
        }
        if(category.getCategoryDepth() == null){
            if(dto.getCategoryParentId() == null){
                category.setCategoryDepth(1);
            } else{
                category.setCategoryDepth(calculateDepth(dto.getCategoryParentId()));
            }
        }
        ProductCategoryEntity saveCategory = productCategoryRepository.save(category);
        return saveCategory.getCategoryId();
    }

    private int calculateDepth(Long categoryParentId) {
        ProductCategoryEntity categoryParent = productCategoryRepository.findById(categoryParentId)
                .orElseThrow(()-> new IllegalArgumentException("상위 카테고리가 존재하지 않습니다."));

        int parentDepth = categoryParent.getCategoryDepth();
        if(parentDepth >= 3){
            throw new IllegalArgumentException("카테고리는 최대 3뎁스까지만 등록 가능합니다.");

        }
        return parentDepth + 1 ;
    }

    @Override
    public List<ProductCategoryResponseDto> getAllCategory() {
        List<ProductCategoryEntity> categories = productCategoryRepository.findAll();

        List<ProductCategoryEntity> topLevel = categories.stream()
                .filter(category -> category.getCategoryParentId() == null).toList();

        return topLevel.stream().map(category -> buildTree(category, categories)).toList();
    }

    private ProductCategoryResponseDto buildTree(ProductCategoryEntity parent, List<ProductCategoryEntity> allCategories) {
        ProductCategoryResponseDto dto = modelMapper.map(parent, ProductCategoryResponseDto.class);

        List<ProductCategoryResponseDto> children = allCategories.stream()
                .filter(child -> {
                    ProductCategoryEntity parentRef = child.getCategoryParentId();
                    return parentRef != null && parentRef.getCategoryId().equals(parent.getCategoryId());
                })
                .map(child -> buildTree(child, allCategories)) // 재귀 호출!
                .toList();

        dto.setChildren(children);
        return dto;
    }
}
