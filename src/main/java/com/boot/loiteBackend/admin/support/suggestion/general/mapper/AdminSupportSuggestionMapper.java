package com.boot.loiteBackend.admin.support.suggestion.general.mapper;

import com.boot.loiteBackend.admin.support.suggestion.general.dto.AdminSupportSuggestionDto;
import com.boot.loiteBackend.domain.support.suggestion.general.entity.SupportSuggestionEntity;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AdminSupportSuggestionMapper {

    private final ModelMapper modelMapper;

    public AdminSupportSuggestionDto toDto(SupportSuggestionEntity entity) {
        return modelMapper.map(entity, AdminSupportSuggestionDto.class);
    }

    public SupportSuggestionEntity toEntity(AdminSupportSuggestionDto dto) {
        return modelMapper.map(dto, SupportSuggestionEntity.class);
    }
}
