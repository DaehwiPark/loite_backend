package com.boot.loiteBackend.admin.product.gift.mapper;

import com.boot.loiteBackend.admin.product.gift.entity.GiftEntity;
import com.boot.loiteBackend.admin.product.gift.entity.ProductGiftEntity;
import com.boot.loiteBackend.admin.product.product.entity.ProductEntity;
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
