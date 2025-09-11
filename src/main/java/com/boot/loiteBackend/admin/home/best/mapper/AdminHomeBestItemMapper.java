package com.boot.loiteBackend.admin.home.best.mapper;

import com.boot.loiteBackend.admin.home.best.dto.AdminHomeBestItemCreateDto;
import com.boot.loiteBackend.admin.home.best.dto.AdminHomeBestItemResponseDto;
import com.boot.loiteBackend.admin.home.best.dto.AdminHomeBestItemUpdateDto;
import com.boot.loiteBackend.domain.home.best.entity.HomeBestItemEntity;
import org.mapstruct.*;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface AdminHomeBestItemMapper {
    @Mapping(target = "id", ignore = true)
    HomeBestItemEntity toEntity(AdminHomeBestItemCreateDto dto);

    /**
     * 부분 업데이트 시 slotNo 는 절대 매핑하지 않도록 명시
     * (슬롯 이동은 서비스에서 별도로 처리)
     */
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mappings({
            @Mapping(target = "slotNo", ignore = true),
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "createdBy", ignore = true),
            @Mapping(target = "updatedBy", ignore = true)
    })
    void updateFromDto(AdminHomeBestItemUpdateDto dto, @MappingTarget HomeBestItemEntity entity);

    AdminHomeBestItemResponseDto toResponseDto(HomeBestItemEntity entity);
}


