package com.boot.loiteBackend.web.product.service;

import com.boot.loiteBackend.admin.product.gift.entity.AdminGiftEntity;
import com.boot.loiteBackend.admin.product.product.entity.AdminProductEntity;
import com.boot.loiteBackend.admin.product.product.entity.AdminProductImageEntity;
import com.boot.loiteBackend.admin.product.product.repository.AdminProductRepository;
import com.boot.loiteBackend.admin.product.section.entity.AdminProductSectionEntity;
import com.boot.loiteBackend.web.product.dto.ProductDetailResponseDto;
import com.boot.loiteBackend.web.product.dto.ProductListResponseDto;
import com.boot.loiteBackend.web.product.dto.ProductMainResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final AdminProductRepository productRepository;

    @Override
    public List<ProductMainResponseDto> getMainProducts() {
        // 최대 10개만 가져오기
        PageRequest top10 = PageRequest.of(0, 10);

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
                            .build();
                })
                .collect(Collectors.toList());
    }

    @Override
    public Page<ProductListResponseDto> getListProducts(String categoryPath, Pageable pageable) {
        Page<AdminProductEntity> productPage = productRepository.findListByCategoryId(categoryPath, pageable);

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
                            .build();
                })
                .toList();

        return new PageImpl<>(dtoList, pageable, productPage.getTotalElements());
    }


    @Override
    public ProductDetailResponseDto getDetailProducts(Long productId) {
        AdminProductEntity entity = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("해당 상품이 존재하지 않습니다."));

        // 썸네일 이미지
        List<String> thumbnailImages = entity.getProductImages().stream()
                .sorted(Comparator.comparingInt(img -> Optional.ofNullable(img.getImageSortOrder()).orElse(9999)))
                .map(AdminProductImageEntity::getImageUrl)
                .toList();

        // 옵션
        List<ProductDetailResponseDto.ProductOptionDto> options = entity.getProductOptions().stream()
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
        List<ProductDetailResponseDto.ProductGiftDto> gifts = entity.getProductGifts().stream()
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
                .productPrice(entity.getProductPrice())
                .discountedPrice(entity.getDiscountedPrice())
                .discountRate(entity.getDiscountRate())
                .deliveryCharge(entity.getProductDeliveryCharge())
                .modelName(entity.getProductModelName())
                .productCode(String.valueOf(entity.getProductId()))
                .productStock(entity.getProductStock())
                .sections(sections)
                .build();
    }
}
