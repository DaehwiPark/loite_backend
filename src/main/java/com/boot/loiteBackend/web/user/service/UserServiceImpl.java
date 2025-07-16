package com.boot.loiteBackend.web.user.service;

import com.boot.loiteBackend.global.error.exception.CustomException;
import com.boot.loiteBackend.global.security.CustomUserDetails;
import com.boot.loiteBackend.web.social.entity.SocialUserEntity;
import com.boot.loiteBackend.web.social.handler.OAuthUnLinkHandlers;
import com.boot.loiteBackend.web.social.repository.SocialUserRepository;
import com.boot.loiteBackend.web.social.resolver.OAuthHandlerResolver;
import com.boot.loiteBackend.web.user.dto.UserCreateRequestDto;
import com.boot.loiteBackend.web.user.entity.UserEntity;
import com.boot.loiteBackend.web.user.error.UserErrorCode;
import com.boot.loiteBackend.web.user.mapper.UserMapper;
import com.boot.loiteBackend.web.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;
    private final SocialUserRepository socialUserRepository;
    private final OAuthHandlerResolver resolver;

    @Override
    public Long signup(UserCreateRequestDto dto) {

        if (userRepository.existsByUserEmail(dto.getUserEmail())) {
            throw new CustomException(UserErrorCode.EMAIL_DUPLICATED);
        }

        if (!dto.getUserPassword().equals(dto.getUserPasswordCheck())) {
            throw new CustomException(UserErrorCode.PASSWORD_MISMATCH);
        }

        LocalDate birthdate;
        try {
            birthdate = LocalDate.parse(dto.getUserBirthdate());  // "yyyy-MM-dd" 형식이어야 함
        } catch (Exception e) {
            throw new CustomException(UserErrorCode.INVALID_BIRTHDATE_FORMAT);
        }

        UserEntity user = userMapper.toEntity(dto);
        user.setUserPassword(dto.getUserPassword());
        user.setUserBirthdate(birthdate);
        user.setEmailVerified(false);
        user.setUserRole("USER");
        user.setUserStatus("ACTIVE");
        user.setUserRegisterType("EMAIL");

        return userRepository.save(user).getUserId();
    }

    @Override
    public void withdraw(CustomUserDetails loginUser, String accessToken) {
        UserEntity user = userRepository.findById(loginUser.getUserId())
                .orElseThrow(() -> new CustomException(UserErrorCode.USER_NOT_FOUND));

        if (!"EMAIL".equalsIgnoreCase(user.getUserRegisterType())) {
            List<SocialUserEntity> socialLinks = socialUserRepository.findAllByUser(user);
            for (SocialUserEntity social : socialLinks) {
                try {
                    String provider = social.getSocialType().toLowerCase();
                    String email = social.getSocialEmail();

                    if (accessToken == null || accessToken.isBlank()) {
                        throw new CustomException(UserErrorCode.SOCIAL_UNLINK_FAILED);
                    }

                    OAuthUnLinkHandlers handler = resolver.resolveUnlink(provider);
                    handler.unlinkSocialAccount(accessToken, email);

                    socialUserRepository.delete(social);
                } catch (Exception e) {
                    throw new CustomException(UserErrorCode.SOCIAL_UNLINK_FAILED);
                }
            }
        }

        user.setUserStatus("DELETED");
        userRepository.save(user);
    }


    private UserEntity getCurrentUser() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        return userRepository.findByUserEmail(email)
                .orElseThrow(() -> new CustomException(UserErrorCode.USER_NOT_FOUND));
    }

    @Override
    public void withdrawById(Long userId) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(UserErrorCode.USER_NOT_FOUND));

        // 소프트 삭제
        user.setUserStatus("DELETED");
        userRepository.save(user);

        // 물리 삭제를 원한다면 아래 한 줄로 대체 가능
        userRepository.delete(user);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean isEmailDuplicated(String userEmail) {
        return userRepository.existsByUserEmail(userEmail);
    }
}
