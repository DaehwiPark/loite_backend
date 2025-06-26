package com.boot.loiteBackend.admin.support.counsel.mapper;

import com.boot.loiteBackend.admin.support.counsel.dto.AdminSupportCounselDto;
import com.boot.loiteBackend.admin.support.counsel.entity.AdminSupportCounselEntity;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AdminSupportCounselMapper {

    private final ModelMapper modelMapper;

    public AdminSupportCounselDto toDto(AdminSupportCounselEntity entity) {
        return modelMapper.map(entity, AdminSupportCounselDto.class);
    }

    public AdminSupportCounselEntity toEntity(AdminSupportCounselDto dto) {
        return modelMapper.map(dto, AdminSupportCounselEntity.class);
    }
}