package com.boot.loiteMsBack.support.suggestion.mapper;

import com.boot.loiteMsBack.support.suggestion.dto.SupportSuggestionDto;
import com.boot.loiteMsBack.support.suggestion.entity.SupportSuggestionEntity;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SupportSuggestionMapper {

    private final ModelMapper modelMapper;

    public SupportSuggestionDto toDto(SupportSuggestionEntity entity) {
        return modelMapper.map(entity, SupportSuggestionDto.class);
    }

    public SupportSuggestionEntity toEntity(SupportSuggestionDto dto) {
        return modelMapper.map(dto, SupportSuggestionEntity.class);
    }
}
