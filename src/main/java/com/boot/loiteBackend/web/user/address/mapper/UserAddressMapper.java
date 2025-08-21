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

    /* Entity -> DTO */
    UserAddressDto toDto(UserAddressEntity entity);

    /* CreateDto -> Entity (userId, isDeleted 등은 서비스에서 보완 세팅) */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "userId", ignore = true)
    @Mapping(target = "isDeleted", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    UserAddressEntity toEntity(UserAddressCreateDto dto);

    /* UpdateDto -> Entity 부분 업데이트 (null 값은 무시) */
    void updateEntityFromDto(UserAddressUpdateDto dto, @MappingTarget UserAddressEntity entity);

    /* (선택) 문자열 트림: null 아니고 빈문자열이면 무시하고 싶다면 @Condition 사용 가능 */
    @Condition
    default boolean hasText(String value) {
        return value != null && !value.trim().isEmpty();
    }

    /* 예: 특정 필드는 빈문자열이면 무시하고 싶다면 수동 매핑 메서드로 분기 가능
       (필요 시 @AfterMapping에서 트림 등 공통 처리)
     */
    @AfterMapping
    default void after(@MappingTarget UserAddressEntity entity) {
        // 공통 후처리 필요 시 (예: alias/recipientName trim 등)
        if (entity.getAlias() != null) entity.setAlias(entity.getAlias().trim());
        if (entity.getReceiverName() != null) entity.setReceiverName(entity.getReceiverName().trim());
        if (entity.getReceiverPhone() != null) entity.setReceiverPhone(entity.getReceiverPhone().trim());
        if (entity.getZipCode() != null) entity.setZipCode(entity.getZipCode().trim());
        if (entity.getAddressLine1() != null) entity.setAddressLine1(entity.getAddressLine1().trim());
        if (entity.getAddressLine2() != null) entity.setAddressLine2(entity.getAddressLine2().trim());
    }
}
