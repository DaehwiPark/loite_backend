package com.boot.loiteBackend.web.user.address.service;

import com.boot.loiteBackend.web.user.address.dto.UserAddressCreateDto;
import com.boot.loiteBackend.web.user.address.dto.UserAddressDto;
import com.boot.loiteBackend.web.user.address.dto.UserAddressUpdateDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserAddressService {
    UserAddressDto create(Long userId, UserAddressCreateDto req);
    UserAddressDto update(Long userId, Long addressId, UserAddressUpdateDto req);
    void delete(Long userId, Long addressId); // soft delete
    Page<UserAddressDto> list(Long userId, String q, Pageable pageable);
    UserAddressDto detail(Long userId, Long addressId);
    UserAddressDto getDefault(Long userId);
    void setDefault(Long userId, Long addressId);
}
