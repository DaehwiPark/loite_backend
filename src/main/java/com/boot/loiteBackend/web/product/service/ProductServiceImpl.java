package com.boot.loiteBackend.web.product.service;

import com.boot.loiteBackend.admin.product.additional.entity.AdminAdditionalEntity;
import com.boot.loiteBackend.admin.product.additional.entity.AdminProductAdditionalEntity;
import com.boot.loiteBackend.admin.product.additional.repository.AdminAdditionalRepository;
import com.boot.loiteBackend.admin.product.additional.repository.AdminProductAdditionalRepository;
import com.boot.loiteBackend.admin.product.category.entity.AdminProductCategoryEntity;
import com.boot.loiteBackend.admin.product.category.repository.AdminProductCategoryRepository;
import com.boot.loiteBackend.admin.product.gift.entity.AdminGiftEntity;
import com.boot.loiteBackend.admin.product.gift.entity.AdminProductGiftEntity;
import com.boot.loiteBackend.admin.product.gift.repository.AdminProductGiftRepository;
import com.boot.loiteBackend.admin.product.option.entity.AdminProductOptionEntity;
import com.boot.loiteBackend.admin.product.option.repository.AdminProductOptionRepository;
import com.boot.loiteBackend.admin.product.product.entity.AdminProductEntity;
import com.boot.loiteBackend.admin.product.product.entity.AdminProductImageEntity;
import com.boot.loiteBackend.admin.product.product.repository.AdminProductRepository;
import com.boot.loiteBackend.admin.product.section.entity.AdminProductSectionEntity;
import com.boot.loiteBackend.web.product.dto.ProductDetailResponseDto;
import com.boot.loiteBackend.web.product.dto.ProductListResponseDto;
import com.boot.loiteBackend.web.product.dto.ProductMainResponseDto;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final AdminProductRepository productRepository;
    private final AdminProductCategoryRepository adminProductCategoryRepository;
    private final AdminProductOptionRepository adminProductOptionRepository;
    private final AdminProductGiftRepository adminProductGiftRepository;
    private final AdminProductAdditionalRepository adminProductAdditionalRepository;

    @Override
    public List<ProductMainResponseDto> getMainProducts() {
        // 최대 10개만 가져오기
        PageRequest top10 = PageRequest.of(0, 24);

        List<AdminProductEntity> products = productRepository.findTop20MainExposedProducts(top10);

        return products.stream()
                .map(entity -> {
                    String imageUrl = entity.getProductImages().stream()
                            .sorted(Comparator.comparingInt(img -> Optional.ofNullable(img.getImageSortOrder()).orElse(9999)))
                            .map(AdminProductImageEntity::getImageUrl)
                            .findFirst()
                            .orElse(null);

                    return ProductMainResponseDto.builder()
                            .productId(entity.getProductId())
                            .productName(entity.getProductName())
                            .productPrice(entity.getProductPrice())
                            .discountedPrice(entity.getDiscountedPrice())
                            .discountRate(entity.getDiscountRate())
                            .imageUrl(imageUrl)
                            .bestProductYn("Y".equals(entity.getBestProductYn()))
                            .newProductYn("Y".equals(entity.getNewProductYn()))
                            .build();
                })
                .collect(Collectors.toList());
    }

    @Override
    public Page<ProductListResponseDto> getListProducts(String categoryPath, String sortType, Pageable pageable) {
        // 1. 부모 카테고리 찾기
        AdminProductCategoryEntity parent = adminProductCategoryRepository.findByCategoryPathAndDeleteYn(categoryPath, "N")
                .orElseThrow(() -> new EntityNotFoundException("카테고리 없음: " + categoryPath));

        // 2. 부모 + 자식 카테고리 path 리스트
        List<String> paths = new ArrayList<>();
        paths.add(parent.getCategoryPath());

        List<AdminProductCategoryEntity> children = adminProductCategoryRepository.findByCategoryParentIdAndDeleteYn(parent, "N");
        paths.addAll(children.stream().map(AdminProductCategoryEntity::getCategoryPath).toList());

        // 3. 정렬 방식 선택
        Page<AdminProductEntity> productPage;
        switch (sortType) {
            case "리뷰많은순":
                productPage = productRepository.findListOrderByReviewCount(paths, pageable);
                break;
            case "신상품순":
                productPage = productRepository.findListByCategoryPaths(paths,
                        PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by(Sort.Direction.DESC, "createdAt")));
                break;
            case "낮은가격순":
                productPage = productRepository.findListByCategoryPaths(paths,
                        PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by(Sort.Direction.ASC, "discountedPrice")));
                break;
            case "높은가격순":
                productPage = productRepository.findListByCategoryPaths(paths,
                        PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by(Sort.Direction.DESC, "discountedPrice")));
                break;
            case "베스트순":
                productPage = productRepository.findListByCategoryPaths(paths,
                        PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by(Sort.Direction.DESC, "salesCount")));
                break;
            case "높은조회순":
                productPage = productRepository.findListByCategoryPaths(paths,
                        PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by(Sort.Direction.DESC, "viewCount")));
                break;
            default:
                throw new IllegalArgumentException("지원하지 않는 정렬 타입: " + sortType);
        }

        // 4. DTO 변환
        List<ProductListResponseDto> dtoList = productPage.getContent().stream()
                .map(entity -> {
                    String imageUrl = entity.getProductImages().stream()
                            .sorted(Comparator.comparingInt(img -> Optional.ofNullable(img.getImageSortOrder()).orElse(9999)))
                            .map(AdminProductImageEntity::getImageUrl)
                            .findFirst()
                            .orElse(null);

                    return ProductListResponseDto.builder()
                            .productId(entity.getProductId())
                            .productName(entity.getProductName())
                            .brandName(entity.getProductBrand() != null ? entity.getProductBrand().getBrandName() : null)
                            .productPrice(entity.getProductPrice())
                            .discountedPrice(entity.getDiscountedPrice())
                            .discountRate(entity.getDiscountRate())
                            .stock(entity.getProductStock())
                            .imageUrl(imageUrl)
                            .soldOutYn(entity.getProductOptions() != null &&
                                    !entity.getProductOptions().isEmpty() &&
                                    entity.getProductOptions().stream().allMatch(opt -> opt.getOptionStock() <= 0))
                            .bestProductYn("Y".equals(entity.getBestProductYn()))
                            .newProductYn("Y".equals(entity.getNewProductYn()))
                            .build();
                })
                .toList();

        return new PageImpl<>(dtoList, pageable, productPage.getTotalElements());
    }


    @Override
    public ProductDetailResponseDto getDetailProducts(Long productId) {
        AdminProductEntity entity = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("해당 상품이 존재하지 않습니다."));

        List<AdminProductOptionEntity> optionCheck = adminProductOptionRepository.findByProductAndDeleteYn(entity, "N");
        List<AdminProductGiftEntity> giftCheck = adminProductGiftRepository.findByProductAndDeleteYn(entity, "N");
        List<AdminProductAdditionalEntity> additionalCheck = adminProductAdditionalRepository.findByProductAndDeleteYn(entity, "N");



        // 썸네일 이미지
        List<String> thumbnailImages = entity.getProductImages().stream()
                .sorted(Comparator.comparingInt(img -> Optional.ofNullable(img.getImageSortOrder()).orElse(9999)))
                .map(AdminProductImageEntity::getImageUrl)
                .toList();

        // 옵션
        List<ProductDetailResponseDto.ProductOptionDto> options = optionCheck.stream()
                .map(option -> ProductDetailResponseDto.ProductOptionDto.builder()
                        .optionId(option.getOptionId())
                        .optionType(option.getOptionType())
                        .optionValue(option.getOptionValue())
                        .optionColorCode(option.getOptionColorCode())
                        .optionAdditionalPrice(option.getOptionAdditionalPrice())
                        .optionStock(option.getOptionStock())
                        .soldOutYn("Y".equalsIgnoreCase(option.getSoldOutYn()))
                        .build())
                .toList();

        // 사은품
        List<ProductDetailResponseDto.ProductGiftDto> gifts = giftCheck.stream()
                .map(pg -> {
                    AdminGiftEntity gift = pg.getGift();
                    return ProductDetailResponseDto.ProductGiftDto.builder()
                            .productGiftId(pg.getProductGiftId())
                            .giftId(gift.getGiftId())
                            .giftName(gift.getGiftName())
                            .giftImageUrl(gift.getGiftImageUrl())
                            .giftStock(gift.getGiftStock())
                            .soldOutYn("Y".equalsIgnoreCase(gift.getSoldOutYn()))
                            .build();
                })
                .toList();

        // 추가구성품
        List<ProductDetailResponseDto.ProductAdditionalDto> additionals = additionalCheck.stream()
                .map(pa -> {
                    AdminAdditionalEntity additional = pa.getAdditional();
                    return ProductDetailResponseDto.ProductAdditionalDto.builder()
                            .productAdditionalId(pa.getProductAdditionalId())
                            .additionalId(additional.getAdditionalId())
                            .additionalName(additional.getAdditionalName())
                            .additionalImageUrl(additional.getAdditionalImageUrl())
                            .additionalStock(additional.getAdditionalStock())
                            .additionalPrice(additional.getAdditionalPrice())
                            .soldOutYn("Y".equalsIgnoreCase(additional.getSoldOutYn()))
                            .build();
                })
                .toList();


        // 상세 섹션
        List<ProductDetailResponseDto.ProductSectionDto> sections = entity.getProductSections().stream()
                .sorted(Comparator.comparingInt(AdminProductSectionEntity::getSectionOrder)) // 정렬
                .map(section -> ProductDetailResponseDto.ProductSectionDto.builder()
                        .sectionType(section.getSectionType())
                        .content(section.getSectionContent())
                        .sectionOrder(section.getSectionOrder())
                        .build())
                .toList();

        return ProductDetailResponseDto.builder()
                .productId(entity.getProductId())
                .brandName(entity.getProductBrand() != null ? entity.getProductBrand().getBrandName() : null)
                .productName(entity.getProductName())
                .thumbnailImages(thumbnailImages)
                .options(options)
                .gifts(gifts)
                .additionals(additionals)
                .productPrice(entity.getProductPrice())
                .discountedPrice(entity.getDiscountedPrice())
                .discountRate(entity.getDiscountRate())
                .deliveryCharge(entity.getProductDeliveryCharge())
                .modelName(entity.getProductModelName())
                .productCode(String.valueOf(entity.getProductId()))
                .productStock(entity.getProductStock())
                .bestProductYn("Y".equals(entity.getBestProductYn()))
                .newProductYn("Y".equals(entity.getNewProductYn()))
                .sections(sections)
                .build();
    }
}
