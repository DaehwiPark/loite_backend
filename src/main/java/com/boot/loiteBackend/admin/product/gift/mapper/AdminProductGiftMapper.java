package com.boot.loiteBackend.admin.product.gift.mapper;

import com.boot.loiteBackend.admin.product.gift.entity.AdminGiftEntity;
import com.boot.loiteBackend.admin.product.gift.entity.AdminProductGiftEntity;
import com.boot.loiteBackend.admin.product.product.entity.AdminProductEntity;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AdminProductGiftMapper {
    private final ModelMapper modelMapper;

    public AdminProductGiftEntity toEntity(AdminGiftEntity gift, AdminProductEntity product) {
        AdminProductGiftEntity entity = new AdminProductGiftEntity();
        entity.setGift(gift);
        entity.setProduct(product);
        return entity;
    }
}
