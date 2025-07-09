package com.boot.loiteBackend.admin.product.product.service;

import com.boot.loiteBackend.admin.product.brand.entity.AdminProductBrandEntity;
import com.boot.loiteBackend.admin.product.brand.repository.AdminProductBrandRepository;
import com.boot.loiteBackend.admin.product.category.entity.AdminProductCategoryEntity;
import com.boot.loiteBackend.admin.product.category.repository.AdminProductCategoryRepository;
import com.boot.loiteBackend.admin.product.general.AdminProductTagId;
import com.boot.loiteBackend.admin.product.gift.entity.AdminGiftEntity;
import com.boot.loiteBackend.admin.product.gift.entity.AdminProductGiftEntity;
import com.boot.loiteBackend.admin.product.gift.mapper.AdminProductGiftMapper;
import com.boot.loiteBackend.admin.product.gift.repository.AdminGiftRepository;
import com.boot.loiteBackend.admin.product.gift.repository.AdminProductGiftRepository;
import com.boot.loiteBackend.admin.product.option.entity.AdminProductOptionEntity;
import com.boot.loiteBackend.admin.product.option.mapper.AdminProductOptionMapper;
import com.boot.loiteBackend.admin.product.option.repository.AdminProductOptionRepository;
import com.boot.loiteBackend.admin.product.product.dto.*;
import com.boot.loiteBackend.admin.product.product.entity.AdminProductEntity;
import com.boot.loiteBackend.admin.product.product.entity.AdminProductImageEntity;
import com.boot.loiteBackend.admin.product.product.enums.ImageType;
import com.boot.loiteBackend.admin.product.product.mapper.AdminProductImageMapper;
import com.boot.loiteBackend.admin.product.product.mapper.AdminProductMapper;
import com.boot.loiteBackend.admin.product.product.repository.AdminProductImageRepository;
import com.boot.loiteBackend.admin.product.product.repository.AdminProductRepository;
import com.boot.loiteBackend.admin.product.product.spec.AdminProductSpecification;
import com.boot.loiteBackend.admin.product.section.dto.AdminProductSectionResponseDto;
import com.boot.loiteBackend.admin.product.section.mapper.AdminProductSectionMapper;
import com.boot.loiteBackend.admin.product.section.service.AdminProductSectionService;
import com.boot.loiteBackend.admin.product.tag.entity.AdminProductTagEntity;
import com.boot.loiteBackend.admin.product.tag.entity.AdminTagEntity;
import com.boot.loiteBackend.admin.product.tag.repository.AdminProductTagRepository;
import com.boot.loiteBackend.admin.product.tag.repository.AdminTagRepository;
import com.boot.loiteBackend.common.file.FileService;
import com.boot.loiteBackend.common.file.FileUploadResult;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class AdminProductServiceImpl implements AdminProductService {

    private final AdminProductRepository adminProductRepository;
    private final AdminProductImageRepository adminProductImageRepository;
    private final AdminProductBrandRepository adminProductBrandRepository;
    private final AdminProductOptionRepository adminProductOptionRepository;
    private final AdminProductTagRepository adminProductTagRepository;
    private final AdminProductCategoryRepository productCategoryRepository;
    private final AdminTagRepository adminTagRepository;
    private final AdminGiftRepository adminGiftRepository;
    private final AdminProductGiftRepository adminProductGiftRepository;
    private final AdminProductMapper adminProductMapper;
    private final AdminProductImageMapper adminProductImageMapper;
    private final AdminProductOptionMapper adminProductOptionMapper;
    private final AdminProductGiftMapper adminProductGiftMapper;
    private final AdminProductSectionMapper adminProductSectionMapper;
    private final FileService fileService;
    private final AdminProductSectionService adminProductSectionService;

    @Override
    public Long saveProduct(AdminProductRequestDto dto, List<MultipartFile> thumbnailImages) {
        //브랜드 조회
        AdminProductBrandEntity brand = adminProductBrandRepository.findById(dto.getProductBrandId())
                .orElseThrow(() -> new IllegalArgumentException("해당 브랜드가 존재하지 않습니다."));

        //카테고리 조회
        AdminProductCategoryEntity category = productCategoryRepository.findById(dto.getCategoryId())
                .orElseThrow(()-> new IllegalArgumentException("해당 카테고리가 존재하지 않습니다."));

        //상품 등록
        AdminProductEntity product = adminProductMapper.toEntity(dto);
        product.setProductBrand(brand);
        product.setProductCategory(category);
        product.setProductOptions(null);

        BigDecimal price = dto.getProductPrice();
        Integer rate = dto.getDiscountRate();
        if (price != null && rate != null) {
            BigDecimal discounted = price
                    .multiply(BigDecimal.valueOf(100 - rate))
                    .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
            product.setDiscountedPrice(discounted);
        }

        AdminProductEntity savedProduct = adminProductRepository.save(product);

        //섹션 등록
        if (dto.getSections() != null && !dto.getSections().isEmpty()) {
            adminProductSectionService.saveSections(savedProduct, dto.getSections());
        }

        //사은품 연결
        if (dto.getGiftIdList() != null && !dto.getGiftIdList().isEmpty()) {
            List<AdminGiftEntity> giftEntities = adminGiftRepository.findAllById(dto.getGiftIdList());

            List<AdminProductGiftEntity> giftMaps = giftEntities.stream()
                    .map(gift -> adminProductGiftMapper.toEntity(gift, savedProduct))
                    .toList();

            adminProductGiftRepository.saveAll(giftMaps);
        }

        // 이미지 등록
        List<AdminProductImageEntity> imageEntities = new ArrayList<>();

        if (thumbnailImages != null && !thumbnailImages.isEmpty()) {
            for (int i = 0; i < thumbnailImages.size(); i++) {
                MultipartFile file = thumbnailImages.get(i);
                if (file == null || file.isEmpty()) continue;

                FileUploadResult result = fileService.save(file, "product");
                if (result == null) continue;

                AdminProductImageRequestDto imageDto = new AdminProductImageRequestDto();
                imageDto.setImageUrl(result.getUrlPath());
                imageDto.setImagePath(result.getPhysicalPath());
                imageDto.setImageType("THUMBNAIL");
                imageDto.setImageSortOrder(i);
                imageDto.setActiveYn("Y");

                AdminProductImageEntity imageEntity = adminProductImageMapper.toEntity(imageDto);
                imageEntity.setProduct(savedProduct);
                imageEntities.add(imageEntity);
            }
        }

        if (!imageEntities.isEmpty()) {
            adminProductImageRepository.saveAll(imageEntities);
        }

        //옵션 연결
        List<AdminProductOptionEntity> optionEntities = dto.getProductOptions().stream()
                .map(optionDto -> {
                    AdminProductOptionEntity option = adminProductOptionMapper.toEntity(optionDto);
                    option.setProduct(savedProduct);
                    option.setSoldOutYn(option.getOptionStock() <= 0 ? "Y" : "N");
                    return option;
                })
                .collect(Collectors.toList());

        adminProductOptionRepository.saveAll(optionEntities);

        //태그 연결
        if (dto.getTagIdList() != null && !dto.getTagIdList().isEmpty()) {
            List<AdminTagEntity> tagEntities = adminTagRepository.findAllById(dto.getTagIdList());

            List<AdminProductTagEntity> tagMaps = tagEntities.stream()
                    .map(tag -> {
                        AdminProductTagEntity map = new AdminProductTagEntity();

                        AdminProductTagId id = new AdminProductTagId();
                        id.setProductId(savedProduct.getProductId());
                        id.setTagId(tag.getTagId());

                        map.setAdminProductTagId(id);
                        map.setProduct(savedProduct);
                        map.setTag(tag);

                        return map;
                    }).toList();

            adminProductTagRepository.saveAll(tagMaps);
        }

        return savedProduct.getProductId();
    }

    @Override
    public void updateProduct(AdminProductRequestDto dto, List<MultipartFile> thumbnailImages) {
        //상품 연결
        AdminProductEntity product = adminProductRepository.findById(dto.getProductId())
                .orElseThrow(()-> new IllegalArgumentException("상품이 존재하지 않습니다."));

        //카테고리 연결
        AdminProductCategoryEntity category = productCategoryRepository.findById(dto.getCategoryId())
                .orElseThrow(()-> new IllegalArgumentException("해당 카테고리가 존재하지 않습니다."));

        //브랜드 연결
        AdminProductBrandEntity brand = adminProductBrandRepository.findById(dto.getProductBrandId())
                .orElseThrow(()-> new IllegalArgumentException("해당 브랜드가 존재하지 않습니다."));

        //브랜드 id 수정
        product.setProductBrand(brand);

        //카테고리 id 수정
        product.setProductCategory(category);

        //상품 필드 수정
        product.setProductName(dto.getProductName());
        product.setProductModelName(dto.getProductModelName());
        product.setProductSummary(dto.getProductSummary());
        product.setProductPrice(dto.getProductPrice());
        product.setProductSupplyPrice(dto.getProductSupplyPrice());
        product.setDiscountRate(dto.getDiscountRate());
        //product.setProductStock(dto.getProductStock());
        product.setProductDeliveryCharge(dto.getProductDeliveryCharge());
        product.setProductFreeDelivery(dto.getProductFreeDelivery());
        product.setActiveYn(dto.getActiveYn());
        product.setDeleteYn(dto.getDeleteYn());
        product.setMainExposureYn(dto.getMainExposureYn());

        BigDecimal price = dto.getProductPrice();
        Integer rate = dto.getDiscountRate();
        if (price != null && rate != null) {
            BigDecimal discounted = price
                    .multiply(BigDecimal.valueOf(100 - rate))
                    .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
            product.setDiscountedPrice(discounted);
        }

        adminProductRepository.save(product);

        //기존 옵션 삭제 후 재삽입
        adminProductOptionRepository.deleteByProduct(product);
        List<AdminProductOptionEntity> optionEntities = dto.getProductOptions().stream()
                .map(optionDto -> {
                    AdminProductOptionEntity option = adminProductOptionMapper.toEntity(optionDto);
                    option.setProduct(product);
                    option.setSoldOutYn(option.getOptionStock() <= 0 ? "Y" : "N");
                    return option;
                }).toList();
        adminProductOptionRepository.saveAll(optionEntities);

        //기존 태그 삭제 후 재삽입
        adminProductTagRepository.deleteByProduct(product);
        if (dto.getTagIdList() != null && !dto.getTagIdList().isEmpty()) {
            List<AdminTagEntity> tagEntities = adminTagRepository.findAllById(dto.getTagIdList());
            List<AdminProductTagEntity> tagMaps = tagEntities.stream()
                    .map(tag -> {
                        AdminProductTagEntity map = new AdminProductTagEntity();
                        map.setProduct(product);
                        map.setTag(tag);
                        map.setAdminProductTagId(new AdminProductTagId(product.getProductId(), tag.getTagId()));
                        return map;
                    }).toList();
            adminProductTagRepository.saveAll(tagMaps);
        }

        // 기존 섹션 삭제 후 재삽입
        if (dto.getSections() != null && !dto.getSections().isEmpty()) {
            adminProductSectionService.updateSections(product, dto.getSections());
        }

        // 기존 이미지 삭제 후 재삽입
        adminProductImageRepository.deleteByProduct(product);

        List<AdminProductImageEntity> imageEntities = new ArrayList<>();

        if (thumbnailImages != null && !thumbnailImages.isEmpty()) {
            for (int i = 0; i < thumbnailImages.size(); i++) {
                MultipartFile file = thumbnailImages.get(i);
                if (file == null || file.isEmpty()) continue;

                FileUploadResult result = fileService.save(file, "product");
                if (result == null) continue;

                AdminProductImageRequestDto imageDto = new AdminProductImageRequestDto();
                imageDto.setImageUrl(result.getUrlPath());
                imageDto.setImagePath(result.getPhysicalPath());
                imageDto.setImageType("THUMBNAIL");
                imageDto.setImageSortOrder(i);
                imageDto.setActiveYn("Y");

                AdminProductImageEntity imageEntity = adminProductImageMapper.toEntity(imageDto);
                imageEntity.setProduct(product);
                imageEntities.add(imageEntity);
            }
        }

        if (!imageEntities.isEmpty()) {
            adminProductImageRepository.saveAll(imageEntities);
        }

    }

    @Override
    public void deleteProduct(Long productId) {
        AdminProductEntity product = adminProductRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("해당 상품이 존재하지 않습니다."));

        product.setDeleteYn("Y");
        adminProductRepository.save(product);
    }

    //상품 목록 조회
    @Override
    @Transactional(readOnly = true)
    public Page<AdminProductListResponseDto> getPagedProducts(String keyword, Pageable pageable) {
        Specification<AdminProductEntity> spec = AdminProductSpecification.isNotDeleted();

        if (StringUtils.hasText(keyword)) {
            spec = spec.and(AdminProductSpecification.containsKeyword(keyword));
        }

        Page<AdminProductEntity> page = adminProductRepository.findAll(spec, pageable);

        return page.map(product -> {
            AdminProductListResponseDto dto = new AdminProductListResponseDto();
            dto.setProductName(product.getProductName());
            dto.setActiveYn(product.getActiveYn());
            dto.setCreatedAt(product.getCreatedAt());
            dto.setProductPrice(product.getProductPrice());
            dto.setDiscountRate(product.getDiscountRate());
            dto.setDiscountedPrice(product.getDiscountedPrice());
            //dto.setProductStock(product.getProductStock());
            dto.setSalesCount(product.getSalesCount());
            dto.setViewCount(product.getViewCount());

            // 연관 엔티티 null 방지
            if (product.getProductBrand() != null) {
                dto.setBrandName(product.getProductBrand().getBrandName());
            }

            return dto;
        });
    }


    //상품 상세 조회
    @Override
    @Transactional(readOnly = true)
    public AdminProductDetailResponseDto getAllProductDetail(Long productId) {
        AdminProductEntity product = adminProductRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("상품이 존재하지 않습니다."));

        AdminProductDetailResponseDto dto = new AdminProductDetailResponseDto();
        dto.setProductId(product.getProductId());
        dto.setProductName(product.getProductName());
        dto.setProductModelName(product.getProductModelName());
        dto.setProductSummary(product.getProductSummary());
        dto.setProductPrice(product.getProductPrice());
        dto.setProductSupplyPrice(product.getProductSupplyPrice());
        dto.setDiscountRate(product.getDiscountRate());
        dto.setDiscountedPrice(product.getDiscountedPrice());
        //dto.setProductStock(product.getProductStock());
        dto.setProductDeliveryCharge(product.getProductDeliveryCharge());
        dto.setProductFreeDelivery(product.getProductFreeDelivery());
        dto.setDeleteYn(product.getDeleteYn());
        dto.setActiveYn(product.getActiveYn());
        dto.setMainExposureYn(product.getMainExposureYn());
        dto.setRecommendedYn(product.getRecommendedYn());

        //섹션
        List<AdminProductSectionResponseDto> sectionDtos = product.getProductSections().stream()
                .map(adminProductSectionMapper::toDto)
                .toList();

        dto.setProductSections(sectionDtos);

        // 브랜드
        if (product.getProductBrand() != null) {
            dto.setProductBrandId(product.getProductBrand().getBrandId());
            dto.setBrandName(product.getProductBrand().getBrandName());
        }

        // 카테고리
        if (product.getProductCategory() != null) {
            dto.setCategoryId(product.getProductCategory().getCategoryId());
            dto.setCategoryName(product.getProductCategory().getCategoryName());
        }

        // 이미지
        List<AdminProductDetailResponseDto.ImageDto> imageDtos = product.getProductImages().stream()
                .map(img -> {
                    AdminProductDetailResponseDto.ImageDto imageDto = new AdminProductDetailResponseDto.ImageDto();
                    imageDto.setImageUrl(img.getImageUrl());
                    imageDto.setImageType(img.getImageType().name());
                    imageDto.setImageSortOrder(img.getImageSortOrder());
                    imageDto.setActiveYn(img.getActiveYn());
                    return imageDto;
                }).toList();
        dto.setProductImages(imageDtos);

        // 옵션
        List<AdminProductDetailResponseDto.ProductOptionDto> optionDtos = product.getProductOptions().stream()
                .map(opt -> {
                    AdminProductDetailResponseDto.ProductOptionDto optDto = new AdminProductDetailResponseDto.ProductOptionDto();
                    optDto.setOptionType(opt.getOptionType());
                    optDto.setOptionValue(opt.getOptionValue());
                    optDto.setOptionAdditionalPrice(opt.getOptionAdditionalPrice());
                    optDto.setOptionStock(opt.getOptionStock());
                    optDto.setOptionStyleType(opt.getOptionStyleType().name());
                    optDto.setActiveYn(opt.getActiveYn());
                    optDto.setOptionSortOrder(opt.getOptionSortOrder());
                    return optDto;
                }).toList();
        dto.setProductOptions(optionDtos);

        // 태그
        List<AdminProductDetailResponseDto.TagDto> tagDtos = product.getProductTags().stream()
                .map(tagMap -> {
                    AdminProductDetailResponseDto.TagDto tagDto = new AdminProductDetailResponseDto.TagDto();
                    tagDto.setTagId(tagMap.getTag().getTagId());
                    tagDto.setTagName(tagMap.getTag().getTagName());
                    return tagDto;
                }).toList();
        dto.setTags(tagDtos);

        return dto;
    }

}
