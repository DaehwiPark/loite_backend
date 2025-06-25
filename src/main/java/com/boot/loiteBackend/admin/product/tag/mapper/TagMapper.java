package com.boot.loiteBackend.admin.product.tag.mapper;

import com.boot.loiteBackend.admin.product.tag.dto.TagRequestDto;
import com.boot.loiteBackend.admin.product.tag.dto.TagResponseDto;
import com.boot.loiteBackend.admin.product.tag.entity.TagEntity;
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
