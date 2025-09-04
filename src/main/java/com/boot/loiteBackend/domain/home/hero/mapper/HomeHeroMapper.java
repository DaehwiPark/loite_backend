package com.boot.loiteBackend.domain.home.hero.mapper;

import com.boot.loiteBackend.admin.home.hero.dto.AdminHomeHeroCreateRequestDto;
import com.boot.loiteBackend.admin.home.hero.dto.AdminHomeHeroResponseDto;
import com.boot.loiteBackend.admin.home.hero.dto.AdminHomeHeroUpdateRequestDto;
import com.boot.loiteBackend.admin.home.hero.enums.PublishStatus;
import com.boot.loiteBackend.domain.home.hero.entity.HomeHeroEntity;
import org.mapstruct.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.time.LocalDateTime;
import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface HomeHeroMapper {

    /* ========== CreateRequestDto → Entity ========== */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", expression = "java(now())")
    @Mapping(target = "updatedAt", expression = "java(now())")
    @Mapping(target = "deletedYn", constant = "N")
    @Mapping(target = "updatedBy", source = "createdBy") // 최초 수정자 = 생성자
    @Mapping(target = "publishStatus", source = "publishStatus", qualifiedByName = "toEnum")
    HomeHeroEntity toEntity(AdminHomeHeroCreateRequestDto dto);

    /* ========== UpdateRequestDto → Entity (merge) ========== */
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "publishStatus", source = "publishStatus", qualifiedByName = "toEnum")
    void updateEntityFromDto(AdminHomeHeroUpdateRequestDto dto, @MappingTarget HomeHeroEntity entity);

    @AfterMapping
    default void afterUpdate(AdminHomeHeroUpdateRequestDto dto, @MappingTarget HomeHeroEntity entity) {
        entity.setUpdatedAt(now());
    }

    /* ========== Entity → Admin ResponseDto ========== */
    @Mapping(target = "publishStatus", source = "publishStatus", qualifiedByName = "toCode")
    AdminHomeHeroResponseDto toAdminResponse(HomeHeroEntity entity);

    List<AdminHomeHeroResponseDto> toAdminResponseList(List<HomeHeroEntity> entities);

    /* ========== Page<Entity> → Page<ResponseDto> ========== */
    default Page<AdminHomeHeroResponseDto> toAdminResponsePage(Page<HomeHeroEntity> entityPage) {
        List<AdminHomeHeroResponseDto> dtoList = entityPage.map(this::toAdminResponse).getContent();
        return new PageImpl<>(dtoList, entityPage.getPageable(), entityPage.getTotalElements());
    }

    /* ===== 타입 변환기: String ↔ Enum(PublishStatus) ===== */
    @Named("toEnum")
    default PublishStatus toEnum(String code) {
        if (code == null || code.isBlank()) return null;
        // null 허용: update 시 null이면 무시(IGNORE 전략) → 기존 값 유지
        for (PublishStatus ps : PublishStatus.values()) {
            if (ps.getCode().equalsIgnoreCase(code)) return ps;
        }
        // 알 수 없는 값이면 예외를 던지거나 기본값으로 처리 — 여기선 예외
        throw new IllegalArgumentException("Unknown publishStatus: " + code);
    }

    @Named("toCode")
    default String toCode(PublishStatus status) {
        return (status == null) ? null : status.getCode();
    }

    /* helper */
    default LocalDateTime now() {
        return LocalDateTime.now();
    }
}
