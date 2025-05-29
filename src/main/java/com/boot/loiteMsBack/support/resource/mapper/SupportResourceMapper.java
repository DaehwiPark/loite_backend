package com.boot.loiteMsBack.support.resource.mapper;

import com.boot.loiteMsBack.support.resource.dto.SupportResourceDto;
import com.boot.loiteMsBack.support.resource.entity.SupportResourceEntity;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SupportResourceMapper {

    private final ModelMapper modelMapper;

    public SupportResourceDto toDto(SupportResourceEntity entity) {
        return modelMapper.map(entity, SupportResourceDto.class);
    }

    public SupportResourceEntity toEntity(SupportResourceDto dto) {
        return modelMapper.map(dto, SupportResourceEntity.class);
    }
}
