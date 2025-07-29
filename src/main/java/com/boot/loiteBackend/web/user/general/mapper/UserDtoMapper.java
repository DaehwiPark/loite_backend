package com.boot.loiteBackend.web.user.general.mapper;

import com.boot.loiteBackend.web.user.general.dto.UserCreateRequestDto;
import com.boot.loiteBackend.web.user.general.dto.UserDto;
import com.boot.loiteBackend.web.user.general.entity.UserEntity;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserDtoMapper {

    private final ModelMapper modelMapper;

    public UserDto toDto(UserEntity entity) {
        return modelMapper.map(entity, UserDto.class);
    }

    public UserEntity toEntity(UserDto dto) {
        return modelMapper.map(dto, UserEntity.class);
    }

    public UserEntity toEntity(UserCreateRequestDto dto) {
        return modelMapper.map(dto, UserEntity.class);
    }
}
