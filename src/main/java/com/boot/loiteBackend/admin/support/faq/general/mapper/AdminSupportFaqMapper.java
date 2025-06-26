package com.boot.loiteBackend.admin.support.faq.general.mapper;

import com.boot.loiteBackend.admin.support.faq.general.dto.AdminSupportFaqDto;
import com.boot.loiteBackend.admin.support.faq.general.entity.AdminSupportFaqEntity;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AdminSupportFaqMapper {

    private final ModelMapper modelMapper;

    public AdminSupportFaqDto toDto(AdminSupportFaqEntity entity) {
        if (entity == null) return null;

        AdminSupportFaqDto dto = modelMapper.map(entity, AdminSupportFaqDto.class);

        if (entity.getFaqCategory() != null) {
            dto.setFaqCategoryId(entity.getFaqCategory().getFaqCategoryId());
            dto.setFaqCategoryName(entity.getFaqCategory().getFaqCategoryName());
        }
        return dto;
    }

    public AdminSupportFaqEntity toEntity(AdminSupportFaqDto dto) {
        if (dto == null) return null;
        AdminSupportFaqEntity entity = modelMapper.map(dto, AdminSupportFaqEntity.class);
        return entity;
    }
}
