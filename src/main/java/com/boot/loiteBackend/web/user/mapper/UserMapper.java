package com.boot.loiteBackend.web.user.mapper;

import com.boot.loiteBackend.web.user.dto.UserCreateRequestDto;
import com.boot.loiteBackend.web.user.dto.UserDto;
import com.boot.loiteBackend.web.user.entity.UserEntity;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserMapper {

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
