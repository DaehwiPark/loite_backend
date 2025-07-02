package com.boot.loiteBackend.web.social.repository;

import com.boot.loiteBackend.web.social.entity.UserSocialEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserSocialRepository extends JpaRepository<UserSocialEntity, Long> {

    // 소셜 타입 + 소셜 유저 번호로 조회
    Optional<UserSocialEntity> findBySocialTypeAndSocialNumber(String socialType, String socialNumber);

    // 특정 유저에 대해 소셜 타입이 이미 연동되었는지 확인
    boolean existsByUserUserIdAndSocialType(Long userId, String socialType);
}
