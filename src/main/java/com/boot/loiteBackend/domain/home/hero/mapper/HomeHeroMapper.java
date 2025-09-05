package com.boot.loiteBackend.domain.home.hero.mapper;

import com.boot.loiteBackend.admin.home.hero.dto.AdminHomeHeroCreateRequestDto;
import com.boot.loiteBackend.admin.home.hero.dto.AdminHomeHeroResponseDto;
import com.boot.loiteBackend.admin.home.hero.dto.AdminHomeHeroUpdateRequestDto;
import com.boot.loiteBackend.domain.home.hero.entity.HomeHeroEntity;
import com.boot.loiteBackend.web.home.hero.dto.HomeHeroResponseDto;
import org.mapstruct.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface HomeHeroMapper {

    /* ===== CreateRequestDto → Entity ===== */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "deletedYn", constant = "N")
    // createdAt/updatedAt 은 DB가 관리 (insertable=false, updatable=false) → 매핑하지 않음
    HomeHeroEntity toEntity(AdminHomeHeroCreateRequestDto dto);

    /** 선택형: 로그인 사용자 ID를 @Context로 받아 감사필드 세팅 */
    default HomeHeroEntity toEntity(AdminHomeHeroCreateRequestDto dto, @Context Long actorUserId) {
        HomeHeroEntity e = toEntity(dto);
        if (actorUserId != null) {
            e.setCreatedBy(actorUserId);
            e.setUpdatedBy(actorUserId);
        }
        return e;
    }

    /* ===== UpdateRequestDto → Entity (PATCH) ===== */
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromDto(AdminHomeHeroUpdateRequestDto dto, @MappingTarget HomeHeroEntity entity);
    // updatedAt 도 DB가 관리 → AfterMapping으로 건드리지 않음

    /* ===== Entity → Admin ResponseDto ===== */
    AdminHomeHeroResponseDto toAdminResponse(HomeHeroEntity entity);
    List<AdminHomeHeroResponseDto> toAdminResponseList(List<HomeHeroEntity> entities);

    default Page<AdminHomeHeroResponseDto> toAdminResponsePage(Page<HomeHeroEntity> entityPage) {
        List<AdminHomeHeroResponseDto> dtoList = entityPage.map(this::toAdminResponse).getContent();
        return new PageImpl<>(dtoList, entityPage.getPageable(), entityPage.getTotalElements());
    }

    /* ===== Entity → Web ResponseDto =====
       엔티티 필드와 동일한 이름은 자동 매핑되지만,
       명시적으로 필요한 필드만 지정해 가독성을 높임 */
    @Mappings({
            @Mapping(source = "id",            target = "id"),
            @Mapping(source = "titleText",     target = "titleText"),
            @Mapping(source = "bodyText",      target = "bodyText"),
            @Mapping(source = "leftImageUrl",  target = "leftImageUrl"),
            // leftImageAlt 컬럼/필드가 제거되었으므로 매핑하지 않음(있어도 null 반환)
            @Mapping(source = "rightTextColor",  target = "rightTextColor"),
            @Mapping(source = "rightBgColor",    target = "rightBgColor"),
            @Mapping(source = "rightBgGradient", target = "rightBgGradient"),
            @Mapping(source = "buttonText",      target = "buttonText"),
            @Mapping(source = "buttonLink",      target = "buttonLink"),
            @Mapping(source = "buttonBgColor",   target = "buttonBgColor"),
            @Mapping(source = "buttonTextColor", target = "buttonTextColor"),
            @Mapping(source = "displayYn",       target = "displayYn"),
            @Mapping(source = "startAt",         target = "startAt"),
            @Mapping(source = "endAt",           target = "endAt"),
            @Mapping(source = "sortOrder",       target = "sortOrder")
    })
    HomeHeroResponseDto toWebResponse(HomeHeroEntity entity);

    List<HomeHeroResponseDto> toWebResponseList(List<HomeHeroEntity> entities);
}
