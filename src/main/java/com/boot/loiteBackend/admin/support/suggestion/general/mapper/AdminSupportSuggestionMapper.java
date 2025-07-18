package com.boot.loiteBackend.admin.support.suggestion.general.mapper;

import com.boot.loiteBackend.admin.support.suggestion.general.dto.AdminSupportSuggestionDto;
import com.boot.loiteBackend.admin.support.suggestion.general.entity.AdminSupportSuggestionEntity;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AdminSupportSuggestionMapper {

    private final ModelMapper modelMapper;

    public AdminSupportSuggestionDto toDto(AdminSupportSuggestionEntity entity) {
        return modelMapper.map(entity, AdminSupportSuggestionDto.class);
    }

    public AdminSupportSuggestionEntity toEntity(AdminSupportSuggestionDto dto) {
        return modelMapper.map(dto, AdminSupportSuggestionEntity.class);
    }
}
