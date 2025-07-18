package com.boot.loiteBackend.admin.product.tag.mapper;

import com.boot.loiteBackend.admin.product.tag.dto.AdminTagRequestDto;
import com.boot.loiteBackend.admin.product.tag.dto.AdminTagResponseDto;
import com.boot.loiteBackend.admin.product.tag.entity.AdminTagEntity;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AdminTagMapper {
    private final ModelMapper modelMapper;

    public AdminTagEntity toEntity(AdminTagRequestDto dto){
        return modelMapper.map(dto, AdminTagEntity.class);
    }

    public AdminTagResponseDto toResponseDto(AdminTagEntity entity){
        return modelMapper.map(entity, AdminTagResponseDto.class);
    }
}
