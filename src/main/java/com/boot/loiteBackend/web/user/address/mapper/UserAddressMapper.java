package com.boot.loiteBackend.web.user.address.mapper;

import com.boot.loiteBackend.domain.user.address.entity.UserAddressEntity;
import com.boot.loiteBackend.web.user.address.dto.UserAddressCreateDto;
import com.boot.loiteBackend.web.user.address.dto.UserAddressDto;
import com.boot.loiteBackend.web.user.address.dto.UserAddressUpdateDto;
import org.mapstruct.*;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface UserAddressMapper {

    /* ===== Entity -> DTO ===== */
    UserAddressDto toDto(UserAddressEntity entity);

    /* ===== CreateDto -> Entity =====
       - id/userId/createdAt/updatedAt은 영속/서비스에서 세팅
       - defaultYn/deleteYn은 서비스 비즈니스 규칙(기본배송지 단일화, 소프트삭제)으로만 세팅 -> 매퍼에서는 무시
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "userId", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "defaultYn", ignore = true)
    @Mapping(target = "deleteYn", ignore = true)
    UserAddressEntity toEntity(UserAddressCreateDto dto);

    /* ===== UpdateDto -> Entity (partial update) =====
       - 문자열 필드: null/빈문자열은 무시 (hasText)
       - defaultYn/deleteYn: 서비스에서만 변경하도록 매퍼에서는 무시
     */
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mappings({
            @Mapping(target = "alias",         source = "alias",         conditionQualifiedByName = "hasText"),
            @Mapping(target = "receiverName",  source = "receiverName",  conditionQualifiedByName = "hasText"),
            @Mapping(target = "receiverPhone", source = "receiverPhone", conditionQualifiedByName = "hasText"),
            @Mapping(target = "zipCode",       source = "zipCode",       conditionQualifiedByName = "hasText"),
            @Mapping(target = "addressLine1",  source = "addressLine1",  conditionQualifiedByName = "hasText"),
            @Mapping(target = "addressLine2",  source = "addressLine2",  conditionQualifiedByName = "hasText"),
            @Mapping(target = "defaultYn", ignore = true),
            @Mapping(target = "deleteYn",  ignore = true)
    })
    void updateEntityFromDto(UserAddressUpdateDto dto, @MappingTarget UserAddressEntity entity);

    /* ===== 공통 유틸 ===== */
    @Named("hasText")
    @Condition
    default boolean hasText(String value) {
        return value != null && !value.trim().isEmpty();
    }

    /* 문자열 공통 trim: 생성/수정 모두 적용 */
    @AfterMapping
    default void after(@MappingTarget UserAddressEntity entity) {
        if (entity.getAlias() != null)         entity.setAlias(entity.getAlias().trim());
        if (entity.getReceiverName() != null)  entity.setReceiverName(entity.getReceiverName().trim());
        if (entity.getReceiverPhone() != null) entity.setReceiverPhone(entity.getReceiverPhone().trim());
        if (entity.getZipCode() != null)       entity.setZipCode(entity.getZipCode().trim());
        if (entity.getAddressLine1() != null)  entity.setAddressLine1(entity.getAddressLine1().trim());
        if (entity.getAddressLine2() != null)  entity.setAddressLine2(entity.getAddressLine2().trim());
    }
}