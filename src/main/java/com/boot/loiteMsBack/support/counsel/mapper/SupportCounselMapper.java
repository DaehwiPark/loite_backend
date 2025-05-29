package com.boot.loiteMsBack.support.counsel.mapper;

import com.boot.loiteMsBack.support.counsel.dto.SupportCounselDto;
import com.boot.loiteMsBack.support.counsel.entity.SupportCounselEntity;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SupportCounselMapper {

    @Autowired
    private ModelMapper modelMapper;

    public SupportCounselDto toDto(SupportCounselEntity entity) {
        return modelMapper.map(entity, SupportCounselDto.class);

    }

    public SupportCounselEntity toEntity(SupportCounselDto dto) {
        return modelMapper.map(dto, SupportCounselEntity.class);
    }
}
