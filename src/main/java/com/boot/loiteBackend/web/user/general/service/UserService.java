package com.boot.loiteBackend.web.user.general.service;

import com.boot.loiteBackend.global.response.ApiResponse;
import com.boot.loiteBackend.global.security.CustomUserDetails;
import com.boot.loiteBackend.web.user.general.dto.UserCreateRequestDto;
import org.springframework.stereotype.Service;

@Service
public interface UserService {

    Long signup(UserCreateRequestDto dto);

    ApiResponse<String> withdraw(CustomUserDetails loginUser, String token);

    void withdrawById(Long userId);

    boolean isEmailDuplicated(String userEmail);

}
