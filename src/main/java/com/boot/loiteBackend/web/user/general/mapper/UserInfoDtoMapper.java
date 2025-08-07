package com.boot.loiteBackend.web.user.general.mapper;

import com.boot.loiteBackend.web.user.general.dto.UserCreateRequestDto;
import com.boot.loiteBackend.web.user.general.dto.UserDto;
import com.boot.loiteBackend.web.user.general.dto.UserInfoDto;
import com.boot.loiteBackend.domain.user.general.entity.UserEntity;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserInfoDtoMapper {
    private final ModelMapper modelMapper;

    public UserInfoDto toDto(UserEntity entity) {
        return modelMapper.map(entity, UserInfoDto.class);
    }

    public UserEntity toEntity(UserDto dto) {
        return modelMapper.map(dto, UserEntity.class);
    }

    public UserEntity toEntity(UserCreateRequestDto dto) {
        return modelMapper.map(dto, UserEntity.class);
    }

}
