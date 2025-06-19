package com.boot.loiteMsBack.product.tag.mapper;

import com.boot.loiteMsBack.product.tag.dto.TagRequestDto;
import com.boot.loiteMsBack.product.tag.dto.TagResponseDto;
import com.boot.loiteMsBack.product.tag.entity.TagEntity;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TagMapper {
    private final ModelMapper modelMapper;

    public TagEntity toEntity(TagRequestDto dto){
        return modelMapper.map(dto, TagEntity.class);
    }

    public TagResponseDto toResponseDto(TagEntity entity){
        return modelMapper.map(entity, TagResponseDto.class);
    }
}
