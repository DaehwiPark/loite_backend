package com.boot.loiteBackend.admin.home.recommend.item.mapper;

import com.boot.loiteBackend.admin.home.recommend.item.dto.AdminHomeRecoItemCreateDto;
import com.boot.loiteBackend.admin.home.recommend.item.dto.AdminHomeRecoItemResponseDto;
import com.boot.loiteBackend.admin.home.recommend.item.dto.AdminHomeRecoItemUpdateDto;
import com.boot.loiteBackend.domain.home.recommend.item.entity.HomeRecoItemEntity;
import org.mapstruct.*;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface AdminHomeRecoItemMapper {

    @Mapping(target = "id", ignore = true)
    HomeRecoItemEntity toEntity(AdminHomeRecoItemCreateDto dto);

    /**
     * 부분 업데이트 시 slotNo/sectionId는 절대 매핑하지 않도록 명시
     * (슬롯 이동은 서비스에서 별도로 처리)
     */
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mappings({
            @Mapping(target = "slotNo", ignore = true),
            @Mapping(target = "sectionId", ignore = true),
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "createdBy", ignore = true),
            @Mapping(target = "updatedBy", ignore = true)
    })
    void updateFromDto(AdminHomeRecoItemUpdateDto dto, @MappingTarget HomeRecoItemEntity entity);

    AdminHomeRecoItemResponseDto toResponseDto(HomeRecoItemEntity entity);
}
