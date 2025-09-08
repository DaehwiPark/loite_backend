package com.boot.loiteBackend.domain.home.fullbanner.mapper;

import com.boot.loiteBackend.admin.home.fullbanner.dto.*;
import com.boot.loiteBackend.domain.home.fullbanner.entity.HomeFullBannerEntity;
import org.mapstruct.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface HomeFullBannerMapper {

    /* Create DTO -> Entity */
    @Mapping(target = "id", ignore = true)
    HomeFullBannerEntity toEntity(AdminHomeFullBannerCreateRequestDto dto);

    /* Update DTO -> Entity (patch) */
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromDto(AdminHomeFullBannerUpdateRequestDto dto, @MappingTarget HomeFullBannerEntity entity);

    /* Entity -> Admin Response */
    AdminHomeFullBannerResponseDto toAdminResponse(HomeFullBannerEntity e);

    List<AdminHomeFullBannerResponseDto> toAdminResponseList(List<HomeFullBannerEntity> list);

    default Page<AdminHomeFullBannerResponseDto> toAdminResponsePage(Page<HomeFullBannerEntity> page) {
        List<AdminHomeFullBannerResponseDto> content = page.map(this::toAdminResponse).getContent();
        return new PageImpl<>(content, page.getPageable(), page.getTotalElements());
    }
}
