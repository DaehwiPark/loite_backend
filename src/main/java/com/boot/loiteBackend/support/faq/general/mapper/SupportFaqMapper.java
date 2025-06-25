package com.boot.loiteBackend.support.faq.general.mapper;

import com.boot.loiteBackend.support.faq.general.dto.SupportFaqDto;
import com.boot.loiteBackend.support.faq.general.entity.SupportFaqEntity;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SupportFaqMapper {

    private final ModelMapper modelMapper;

    public SupportFaqDto toDto(SupportFaqEntity entity) {
        if (entity == null) return null;

        SupportFaqDto dto = modelMapper.map(entity, SupportFaqDto.class);

        if (entity.getFaqCategory() != null) {
            dto.setFaqCategoryId(entity.getFaqCategory().getFaqCategoryId());
            dto.setFaqCategoryName(entity.getFaqCategory().getFaqCategoryName());
        }
        return dto;
    }

    public SupportFaqEntity toEntity(SupportFaqDto dto) {
        if (dto == null) return null;
        SupportFaqEntity entity = modelMapper.map(dto, SupportFaqEntity.class);
        return entity;
    }
}
