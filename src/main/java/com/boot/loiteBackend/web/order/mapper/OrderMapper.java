package com.boot.loiteBackend.web.order.mapper;

import com.boot.loiteBackend.web.order.dto.*;
import com.boot.loiteBackend.web.order.entity.*;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface OrderMapper {

    // OrderEntity -> OrderResponseDto
    @Mapping(source = "orderItems", target = "items")
    @Mapping(target = "payAmount", expression = "java(entity.getTotalAmount())")
    @Mapping(target = "paymentMethod", ignore = true)
    @Mapping(target = "pgProvider", ignore = true)
    @Mapping(target = "cardCompany", ignore = true)
    @Mapping(target = "cardBrand", ignore = true)
    OrderResponseDto toDto(OrderEntity entity);

    // OrderItemEntity -> OrderItemResponseDto
    @Mapping(source = "product.productId", target = "productId")
    @Mapping(source = "product.productName", target = "productName")
    @Mapping(target = "productImageUrl", ignore = true)
    OrderItemResponseDto toDto(OrderItemEntity entity);

    // 옵션
    @Mapping(source = "productOption.optionId", target = "optionId")
    @Mapping(source = "productOption.optionValue", target = "optionValue")
    @Mapping(source = "additionalPrice", target = "additionalPrice")
    OrderOptionResponseDto toDto(OrderItemOptionEntity entity);

    // 추가구성품
    @Mapping(source = "productAdditional.additional.additionalId", target = "additionalId")
    @Mapping(source = "productAdditional.additional.additionalName", target = "additionalName")
    @Mapping(source = "additionalPrice", target = "additionalPrice")
    @Mapping(target = "additionalImageUrl", ignore = true)
    OrderAdditionalResponseDto toDto(OrderItemAdditionalEntity entity);

    // 사은품
    @Mapping(source = "productGift.gift.giftId", target = "giftId")
    @Mapping(source = "productGift.gift.giftName", target = "giftName")
    @Mapping(target = "giftImageUrl", ignore = true)
    OrderGiftResponseDto toDto(OrderItemGiftEntity entity);
}

