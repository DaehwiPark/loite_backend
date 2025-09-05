package com.boot.loiteBackend.domain.home.topbar.mapper;

import com.boot.loiteBackend.admin.home.topbar.dto.*;
import com.boot.loiteBackend.domain.home.topbar.entity.HomeTopBarEntity;
import com.boot.loiteBackend.web.home.topbar.dto.HomeTopBarResponseDto;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface HomeTopBarMapper {

    // ===== Admin (이미 존재) =====
    @Mapping(target = "id", ignore = true)
    HomeTopBarEntity toEntity(AdminHomeTopBarCreateRequestDto dto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromDto(AdminHomeTopBarUpdateRequestDto dto, @MappingTarget HomeTopBarEntity entity);

    AdminHomeTopBarResponseDto toResponse(HomeTopBarEntity entity);

    // ===== Web (★ 추가) =====
    @Mappings({
            @Mapping(source = "id",                  target = "id"),
            @Mapping(source = "text",                target = "text"),            // Entity 필드명 확인: 보통 homeTopBarText → text
            @Mapping(source = "backgroundColor",     target = "backgroundColor"), // HOME_BACKGROUND_COLOR
            @Mapping(source = "textColor",           target = "textColor")        // HOME_TEXT_COLOR
    })
    HomeTopBarResponseDto toWebResponse(HomeTopBarEntity entity);

    List<HomeTopBarResponseDto> toWebResponseList(List<HomeTopBarEntity> entities);
}
