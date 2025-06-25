package com.boot.loiteBackend.admin.product.product.service;

import com.boot.loiteBackend.admin.product.brand.entity.ProductBrandEntity;
import com.boot.loiteBackend.admin.product.brand.repository.ProductBrandRepository;
import com.boot.loiteBackend.admin.product.category.entity.ProductCategoryEntity;
import com.boot.loiteBackend.admin.product.category.repository.ProductCategoryRepository;
import com.boot.loiteBackend.admin.product.general.ProductTagId;
import com.boot.loiteBackend.admin.product.option.entity.ProductOptionEntity;
import com.boot.loiteBackend.admin.product.option.mapper.ProductOptionMapper;
import com.boot.loiteBackend.admin.product.option.repository.ProductOptionRepository;
import com.boot.loiteBackend.admin.product.product.dto.ProductDetailResponseDto;
import com.boot.loiteBackend.admin.product.product.dto.ProductImageRequestDto;
import com.boot.loiteBackend.admin.product.product.dto.ProductListResponseDto;
import com.boot.loiteBackend.admin.product.product.dto.ProductRequestDto;
import com.boot.loiteBackend.admin.product.product.entity.ProductEntity;
import com.boot.loiteBackend.admin.product.product.entity.ProductImageEntity;
import com.boot.loiteBackend.admin.product.product.enums.ImageType;
import com.boot.loiteBackend.admin.product.product.mapper.ProductImageMapper;
import com.boot.loiteBackend.admin.product.product.mapper.ProductMapper;
import com.boot.loiteBackend.admin.product.product.repository.ProductImageRepository;
import com.boot.loiteBackend.admin.product.product.repository.ProductRepository;
import com.boot.loiteBackend.admin.product.product.spec.ProductSpecification;
import com.boot.loiteBackend.admin.product.tag.entity.ProductTagEntity;
import com.boot.loiteBackend.admin.product.tag.entity.TagEntity;
import com.boot.loiteBackend.admin.product.tag.repository.ProductTagRepository;
import com.boot.loiteBackend.admin.product.tag.repository.TagRepository;
import com.boot.loiteBackend.util.file.FileService;
import com.boot.loiteBackend.util.file.FileUploadResult;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final ProductImageRepository productImageRepository;
    private final ProductBrandRepository productBrandRepository;
    private final ProductOptionRepository productOptionRepository;
    private final ProductTagRepository productTagRepository;
    private final ProductCategoryRepository productCategoryRepository;
    private final TagRepository tagRepository;
    private final ProductMapper productMapper;
    private final ProductImageMapper productImageMapper;
    private final ProductOptionMapper productOptionMapper;
    private final FileService fileService;

    @Override
    public Long saveProduct(ProductRequestDto dto, List<MultipartFile> thumbnailImages, List<MultipartFile> detailImages, Integer mainIndex) throws IOException {
        //브랜드 조회
        ProductBrandEntity brand = productBrandRepository.findById(dto.getProductBrandId())
                .orElseThrow(() -> new IllegalArgumentException("해당 브랜드가 존재하지 않습니다."));

        //카테고리 조회
        ProductCategoryEntity category = productCategoryRepository.findById(dto.getCategoryId())
                .orElseThrow(()-> new IllegalArgumentException("해당 카테고리가 존재하지 않습니다."));

        //상품 등록
        ProductEntity product = productMapper.toEntity(dto);
        product.setProductBrand(brand);
        ProductEntity savedProduct = productRepository.save(product);

        // 이미지 등록
        List<ProductImageEntity> imageEntities = new ArrayList<>();

        // 썸네일
        if (thumbnailImages != null && !thumbnailImages.isEmpty()) {
            for (int i = 0; i < thumbnailImages.size(); i++) {
                MultipartFile file = thumbnailImages.get(i);
                if (file == null || file.isEmpty()) continue;

                FileUploadResult result = fileService.save(file, "product");
                if (result == null) continue;

                ImageType type = (i == mainIndex) ? ImageType.MAIN : ImageType.THUMBNAIL;

                ProductImageRequestDto imageDto = new ProductImageRequestDto();
                imageDto.setImageUrl(result.getUrlPath());
                imageDto.setImagePath(result.getPhysicalPath());
                imageDto.setImageType(type.name());
                imageDto.setImageSortOrder(i + 1);
                imageDto.setActiveYn("Y");

                ProductImageEntity imageEntity = productImageMapper.toEntity(imageDto);
                imageEntity.setProduct(savedProduct);
                imageEntities.add(imageEntity);
            }
        }

        // 상세
        if (detailImages != null && !detailImages.isEmpty()) {
            for (int i = 0; i < detailImages.size(); i++) {
                MultipartFile file = detailImages.get(i);
                if (file == null || file.isEmpty()) continue;

                FileUploadResult result = fileService.save(file, "product");
                if (result == null) continue;

                ProductImageRequestDto imageDto = new ProductImageRequestDto();
                imageDto.setImageUrl(result.getUrlPath());
                imageDto.setImagePath(result.getPhysicalPath());
                imageDto.setImageType(ImageType.DETAIL.name());
                imageDto.setImageSortOrder(i + 1);
                imageDto.setActiveYn("Y");

                ProductImageEntity imageEntity = productImageMapper.toEntity(imageDto);
                imageEntity.setProduct(savedProduct);
                imageEntities.add(imageEntity);
            }
        }
        if (!imageEntities.isEmpty()) {
            productImageRepository.saveAll(imageEntities);
        }

        //옵션 연결
        List<ProductOptionEntity> optionEntities = dto.getProductOptions().stream()
                .map(optionDto -> {
                    ProductOptionEntity option = productOptionMapper.toEntity(optionDto);
                    option.setProduct(savedProduct);
                    return option;
                })
                .collect(Collectors.toList());
        productOptionRepository.saveAll(optionEntities);

        //태그 연결
        if (dto.getTagIdList() != null && !dto.getTagIdList().isEmpty()) {
            List<TagEntity> tagEntities = tagRepository.findAllById(dto.getTagIdList());

            List<ProductTagEntity> tagMaps = tagEntities.stream()
                    .map(tag -> {
                        ProductTagEntity map = new ProductTagEntity();

                        ProductTagId id = new ProductTagId();
                        id.setProductId(savedProduct.getProductId());
                        id.setTagId(tag.getTagId());

                        map.setProductTagId(id);
                        map.setProduct(savedProduct);
                        map.setTag(tag);

                        return map;
                    }).toList();

            productTagRepository.saveAll(tagMaps);
        }

        return savedProduct.getProductId();
    }

    @Override
    public void updateProduct(ProductRequestDto dto, List<MultipartFile> thumbnailImages, List<MultipartFile> detailImages, Integer mainIndex) throws IOException {
        //상품 연결
        ProductEntity product = productRepository.findById(dto.getProductId())
                .orElseThrow(()-> new IllegalArgumentException("상품이 존재하지 않습니다."));

        //카테고리 연결
        ProductCategoryEntity category = productCategoryRepository.findById(dto.getCategoryId())
                .orElseThrow(()-> new IllegalArgumentException("해당 카테고리가 존재하지 않습니다."));

        //브랜드 연결
        ProductBrandEntity brand = productBrandRepository.findById(dto.getProductBrandId())
                .orElseThrow(()-> new IllegalArgumentException("해당 브랜드가 존재하지 않습니다."));

        //브랜드 id 수정
        product.setProductBrand(brand);

        //카테고리 id 수정
        product.setProductCategory(category);

        //상품 필드 수정
        product.setProductName(dto.getProductName());
        product.setProductModelName(dto.getProductModelName());
        product.setProductSummary(dto.getProductSummary());
        product.setProductDescription(dto.getProductDescription());
        product.setProductPrice(dto.getProductPrice());
        product.setProductSupplyPrice(dto.getProductSupplyPrice());
        product.setProductStock(dto.getProductStock());
        product.setProductDeliveryCharge(dto.getProductDeliveryCharge());
        product.setProductFreeDelivery(dto.getProductFreeDelivery());
        product.setActiveYn(dto.getActiveYn());
        product.setDeleteYn(dto.getDeleteYn());

        productRepository.save(product);

        //기존 옵션 삭제 후 재삽입
        productOptionRepository.deleteByProduct(product);
        List<ProductOptionEntity> optionEntities = dto.getProductOptions().stream()
                .map(optionDto -> {
                    ProductOptionEntity option = productOptionMapper.toEntity(optionDto);
                    option.setProduct(product);
                    return option;
                }).toList();
        productOptionRepository.saveAll(optionEntities);

        //기존 태그 삭제 후 재삽입
        productTagRepository.deleteByProduct(product);
        if (dto.getTagIdList() != null && !dto.getTagIdList().isEmpty()) {
            List<TagEntity> tagEntities = tagRepository.findAllById(dto.getTagIdList());
            List<ProductTagEntity> tagMaps = tagEntities.stream()
                    .map(tag -> {
                        ProductTagEntity map = new ProductTagEntity();
                        map.setProduct(product);
                        map.setTag(tag);
                        map.setProductTagId(new ProductTagId(product.getProductId(), tag.getTagId()));
                        return map;
                    }).toList();
            productTagRepository.saveAll(tagMaps);
        }

        //기존 이미지 삭제 후 재삽입
        productImageRepository.deleteByProduct(product);
        List<ProductImageEntity> imageEntities = new ArrayList<>();

        // 썸네일
        if (thumbnailImages != null && !thumbnailImages.isEmpty()) {
            for (int i = 0; i < thumbnailImages.size(); i++) {
                MultipartFile file = thumbnailImages.get(i);
                if (file == null || file.isEmpty()) continue;

                FileUploadResult result = fileService.save(file, "product");
                if (result == null) continue;

                ImageType type = (i == mainIndex) ? ImageType.MAIN : ImageType.THUMBNAIL;

                ProductImageRequestDto imageDto = new ProductImageRequestDto();
                imageDto.setImageUrl(result.getUrlPath());
                imageDto.setImagePath(result.getPhysicalPath());
                imageDto.setImageType(type.name());
                imageDto.setImageSortOrder(i + 1);
                imageDto.setActiveYn("Y");

                ProductImageEntity imageEntity = productImageMapper.toEntity(imageDto);
                imageEntity.setProduct(product);
                imageEntities.add(imageEntity);
            }
        }

        // 상세
        if (detailImages != null && !detailImages.isEmpty()) {
            for (int i = 0; i < detailImages.size(); i++) {
                MultipartFile file = detailImages.get(i);
                if (file == null || file.isEmpty()) continue;

                FileUploadResult result = fileService.save(file, "product");
                if (result == null) continue;

                ProductImageRequestDto imageDto = new ProductImageRequestDto();
                imageDto.setImageUrl(result.getUrlPath());
                imageDto.setImagePath(result.getPhysicalPath());
                imageDto.setImageType(ImageType.DETAIL.name());
                imageDto.setImageSortOrder(i + 1);
                imageDto.setActiveYn("Y");

                ProductImageEntity imageEntity = productImageMapper.toEntity(imageDto);
                imageEntity.setProduct(product);
                imageEntities.add(imageEntity);
            }
        }
        if (!imageEntities.isEmpty()) {
            productImageRepository.saveAll(imageEntities);
        }
    }

    @Override
    public void deleteProduct(Long productId) {
        ProductEntity product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("해당 상품이 존재하지 않습니다."));

        product.setDeleteYn("Y");
        productRepository.save(product);
    }

    //상품 목록 조회
    @Override
    @Transactional(readOnly = true)
    public Page<ProductListResponseDto> getPagedProducts(String keyword, Pageable pageable) {
        Specification<ProductEntity> spec = ProductSpecification.isNotDeleted();

        if (StringUtils.hasText(keyword)) {
            spec = spec.and(ProductSpecification.containsKeyword(keyword));
        }

        Page<ProductEntity> page = productRepository.findAll(spec, pageable);

        return page.map(product -> {
            ProductListResponseDto dto = new ProductListResponseDto();
            dto.setProductName(product.getProductName());
            dto.setActiveYn(product.getActiveYn());
            dto.setCreatedAt(product.getCreatedAt());
            dto.setProductPrice(product.getProductPrice());
            dto.setProductStock(product.getProductStock());
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
    public ProductDetailResponseDto getAllProductDetail(Long productId) {
        ProductEntity product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("상품이 존재하지 않습니다."));

        ProductDetailResponseDto dto = new ProductDetailResponseDto();
        dto.setProductId(product.getProductId());
        dto.setProductName(product.getProductName());
        dto.setProductModelName(product.getProductModelName());
        dto.setProductSummary(product.getProductSummary());
        dto.setProductDescription(product.getProductDescription());
        dto.setProductPrice(product.getProductPrice());
        dto.setProductSupplyPrice(product.getProductSupplyPrice());
        dto.setProductStock(product.getProductStock());
        dto.setProductDeliveryCharge(product.getProductDeliveryCharge());
        dto.setProductFreeDelivery(product.getProductFreeDelivery());
        dto.setDeleteYn(product.getDeleteYn());
        dto.setActiveYn(product.getActiveYn());
        dto.setRecommendedYn(product.getRecommendedYn());

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
        List<ProductDetailResponseDto.ImageDto> imageDtos = product.getProductImages().stream()
                .map(img -> {
                    ProductDetailResponseDto.ImageDto imageDto = new ProductDetailResponseDto.ImageDto();
                    imageDto.setImageUrl(img.getImageUrl());
                    imageDto.setImageType(img.getImageType().name());
                    imageDto.setImageSortOrder(img.getImageSortOrder());
                    imageDto.setActiveYn(img.getActiveYn());
                    return imageDto;
                }).toList();
        dto.setProductImages(imageDtos);

        // 옵션
        List<ProductDetailResponseDto.ProductOptionDto> optionDtos = product.getProductOptions().stream()
                .map(opt -> {
                    ProductDetailResponseDto.ProductOptionDto optDto = new ProductDetailResponseDto.ProductOptionDto();
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
        List<ProductDetailResponseDto.TagDto> tagDtos = product.getProductTags().stream()
                .map(tagMap -> {
                    ProductDetailResponseDto.TagDto tagDto = new ProductDetailResponseDto.TagDto();
                    tagDto.setTagId(tagMap.getTag().getTagId());
                    tagDto.setTagName(tagMap.getTag().getTagName());
                    return tagDto;
                }).toList();
        dto.setTags(tagDtos);

        return dto;
    }

}
