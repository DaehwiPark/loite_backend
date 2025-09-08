package com.boot.loiteBackend.domain.home.eventbanner.mapper;

import com.boot.loiteBackend.admin.home.eventbanner.dto.*;
import com.boot.loiteBackend.domain.home.eventbanner.entity.HomeEventBannerEntity;
import com.boot.loiteBackend.web.home.eventbanner.dto.HomeEventBannerResponseDto;
import org.mapstruct.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.time.LocalDateTime;
import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface HomeEventBannerMapper {

    // Create
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", expression = "java(now())")
    @Mapping(target = "updatedAt", expression = "java(now())")
    HomeEventBannerEntity toEntity(AdminHomeEventBannerCreateRequestDto dto);

    // Update (merge)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromDto(AdminHomeEventBannerUpdateRequestDto dto, @MappingTarget HomeEventBannerEntity entity);

    @AfterMapping
    default void afterUpdate(AdminHomeEventBannerUpdateRequestDto dto, @MappingTarget HomeEventBannerEntity entity) {
        entity.setUpdatedAt(now());
    }

    // Admin Response
    AdminHomeEventBannerResponseDto toAdminResponse(HomeEventBannerEntity e);
    List<AdminHomeEventBannerResponseDto> toAdminResponseList(List<HomeEventBannerEntity> list);

    default Page<AdminHomeEventBannerResponseDto> toAdminResponsePage(Page<HomeEventBannerEntity> entityPage) {
        List<AdminHomeEventBannerResponseDto> dtoList = entityPage.map(this::toAdminResponse).getContent();
        return new PageImpl<>(dtoList, entityPage.getPageable(), entityPage.getTotalElements());
    }

    // Web Response
    @Mappings({
            @Mapping(source = "id", target = "id"),
            @Mapping(source = "bannerTitle", target = "bannerTitle"),
            @Mapping(source = "pcImageUrl", target = "pcImageUrl"),
            @Mapping(source = "mobileImageUrl", target = "mobileImageUrl"),
            @Mapping(source = "linkUrl", target = "linkUrl"),
            @Mapping(source = "linkTarget", target = "linkTarget"),
            @Mapping(source = "startAt", target = "startAt"),
            @Mapping(source = "endAt", target = "endAt"),
            @Mapping(source = "defaultSlot", target = "defaultSlot")
    })
    HomeEventBannerResponseDto toWebResponse(HomeEventBannerEntity e);
    List<HomeEventBannerResponseDto> toWebResponseList(List<HomeEventBannerEntity> list);

    default LocalDateTime now() { return LocalDateTime.now(); }
}
