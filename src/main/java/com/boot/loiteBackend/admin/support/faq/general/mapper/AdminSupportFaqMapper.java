package com.boot.loiteBackend.admin.support.faq.general.mapper;

import com.boot.loiteBackend.admin.support.faq.general.dto.AdminSupportFaqDto;
import com.boot.loiteBackend.domain.support.faq.general.entity.SupportFaqEntity;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AdminSupportFaqMapper {

    private final ModelMapper modelMapper;

    public AdminSupportFaqDto toDto(SupportFaqEntity entity) {
        if (entity == null) return null;

        AdminSupportFaqDto dto = modelMapper.map(entity, AdminSupportFaqDto.class);

        if (entity.getFaqCategory() != null) {
            dto.setFaqMediumCategoryId(entity.getFaqCategory().getFaqMediumCategoryId());
            dto.setFaqMediumCategoryName(entity.getFaqCategory().getFaqMediumCategoryName());

            if (entity.getFaqCategory().getFaqMajorCategory() != null) {
                dto.setFaqMajorCategoryId(entity.getFaqCategory().getFaqMajorCategory().getFaqMajorCategoryId());
                dto.setFaqMajorCategoryName(entity.getFaqCategory().getFaqMajorCategory().getFaqMajorCategoryName());
            }
        }

        return dto;
    }

    public SupportFaqEntity toEntity(AdminSupportFaqDto dto) {
        if (dto == null) return null;
        return modelMapper.map(dto, SupportFaqEntity.class);
    }
}
