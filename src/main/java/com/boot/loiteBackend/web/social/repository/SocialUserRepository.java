package com.boot.loiteBackend.web.social.repository;

import com.boot.loiteBackend.domain.social.entity.SocialUserEntity;
import com.boot.loiteBackend.domain.user.general.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SocialUserRepository extends JpaRepository<SocialUserEntity, Long> {

    Optional<SocialUserEntity> findBySocialTypeAndSocialNumber(String socialType, String socialNumber);

    List<SocialUserEntity> findAllByUser(UserEntity user);

    Optional<SocialUserEntity> findByUserAndSocialType(UserEntity user, String upperCase);

}
