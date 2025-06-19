package com.boot.loiteMsBack.support.counsel.mapper;

import com.boot.loiteMsBack.support.counsel.dto.SupportCounselDto;
import com.boot.loiteMsBack.support.counsel.entity.SupportCounselEntity;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SupportCounselMapper {

    private final ModelMapper modelMapper;

    public SupportCounselDto toDto(SupportCounselEntity entity) {
        return modelMapper.map(entity, SupportCounselDto.class);
    }

    public SupportCounselEntity toEntity(SupportCounselDto dto) {
        return modelMapper.map(dto, SupportCounselEntity.class);
    }
}