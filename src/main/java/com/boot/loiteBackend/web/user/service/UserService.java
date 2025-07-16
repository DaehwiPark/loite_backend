package com.boot.loiteBackend.web.user.service;

import com.boot.loiteBackend.global.security.CustomUserDetails;
import com.boot.loiteBackend.web.user.dto.UserCreateRequestDto;
import org.springframework.stereotype.Service;

@Service
public interface UserService {

    Long signup(UserCreateRequestDto dto);

    void withdraw(CustomUserDetails loginUser, String token);

    void withdrawById(Long userId);

    boolean isEmailDuplicated(String userEmail);

}
