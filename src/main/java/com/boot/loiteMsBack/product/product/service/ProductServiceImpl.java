package com.boot.loiteMsBack.product.product.service;

import com.boot.loiteMsBack.product.brand.entity.ProductBrandEntity;
import com.boot.loiteMsBack.product.brand.repository.ProductBrandRepository;
import com.boot.loiteMsBack.product.category.entity.ProductCategoryEntity;
import com.boot.loiteMsBack.product.category.repository.ProductCategoryRepository;
import com.boot.loiteMsBack.product.general.FileUploadUtil;
import com.boot.loiteMsBack.product.general.ProductTagId;
import com.boot.loiteMsBack.product.option.dto.ProductOptionRequestDto;
import com.boot.loiteMsBack.product.option.entity.ProductOptionEntity;
import com.boot.loiteMsBack.product.option.mapper.ProductOptionMapper;
import com.boot.loiteMsBack.product.option.repository.ProductOptionRepository;
import com.boot.loiteMsBack.product.product.dto.ProductImageRequestDto;
import com.boot.loiteMsBack.product.product.dto.ProductRequestDto;
import com.boot.loiteMsBack.product.product.entity.ProductEntity;
import com.boot.loiteMsBack.product.product.entity.ProductImageEntity;
import com.boot.loiteMsBack.product.product.enums.ImageType;
import com.boot.loiteMsBack.product.product.mapper.ProductImageMapper;
import com.boot.loiteMsBack.product.product.mapper.ProductMapper;
import com.boot.loiteMsBack.product.product.repository.ProductImageRepository;
import com.boot.loiteMsBack.product.product.repository.ProductRepository;
import com.boot.loiteMsBack.product.tag.entity.ProductTagEntity;
import com.boot.loiteMsBack.product.tag.entity.TagEntity;
import com.boot.loiteMsBack.product.tag.repository.ProductTagRepository;
import com.boot.loiteMsBack.product.tag.repository.TagRepository;
import com.boot.loiteMsBack.util.file.FileService;
import com.boot.loiteMsBack.util.file.FileUploadResult;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
        for (int i = 0; i < thumbnailImages.size(); i++) {
            MultipartFile file = thumbnailImages.get(i);
            FileUploadResult result = fileService.save(file, "product");

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
        for (int i = 0; i < detailImages.size(); i++) {
            MultipartFile file = detailImages.get(i);
            FileUploadResult result = fileService.save(file, "product");

            ProductImageRequestDto imageDto = new ProductImageRequestDto();
            imageDto.setImageUrl(result.getUrlPath());
            imageDto.setImagePath(result.getPhysicalPath());
            imageDto.setImageType(ImageType.DETAIL.name());
            imageDto.setImageSortOrder(i + 1);
            imageDto.setActiveYn("Y");

            ProductImageEntity imageEntity = productImageMapper.toEntity(imageDto);
            imageEntity.setProduct(savedProduct); // 연관관계 설정
            imageEntities.add(imageEntity);
        }
        productImageRepository.saveAll(imageEntities);

        //옵션 연결
        List<ProductOptionEntity> optionEntities = dto.getProductOptions().stream()
                .map(optionDto -> {
                    ProductOptionEntity option = productOptionMapper.toEntity(optionDto);
                    option.setProduct(savedProduct); // 연관관계 설정
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

                        // 1. 복합키 객체 생성해서 값 넣기
                        ProductTagId id = new ProductTagId();
                        id.setProductId(savedProduct.getProductId());
                        id.setTagId(tag.getTagId());

                        // 2. 엔티티 세팅
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
    public void updateProduct(Long productId, ProductRequestDto dto) {

    }
}
