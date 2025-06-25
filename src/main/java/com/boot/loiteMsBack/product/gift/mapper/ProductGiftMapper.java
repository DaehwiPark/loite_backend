package com.boot.loiteMsBack.product.gift.mapper;

import com.boot.loiteMsBack.product.gift.entity.GiftEntity;
import com.boot.loiteMsBack.product.gift.entity.ProductGiftEntity;
import com.boot.loiteMsBack.product.product.entity.ProductEntity;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ProductGiftMapper {
    private final ModelMapper modelMapper;

    public ProductGiftEntity toEntity(GiftEntity gift, ProductEntity product) {
        ProductGiftEntity entity = new ProductGiftEntity();
        entity.setGift(gift);
        entity.setProduct(product);
        return entity;
    }
}
