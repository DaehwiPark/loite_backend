package com.boot.loiteBackend.web.user.general.repository;

import com.boot.loiteBackend.domain.user.general.entity.UserEntity;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Long> {

    boolean existsByUserEmail(String userEmail);

    boolean existsByUserPhone(String userPhone);

    Optional<UserEntity> findByUserEmail(String userEmail);

    Optional<UserEntity> findByUserNameAndUserPhone(String userName, String userPhone);

    boolean existsByUserEmailAndUserNameAndUserPhone(String userEmail, String userName, String userPhone);

    @EntityGraph(attributePaths = {"userRole", "userStatus"})
    Optional<UserEntity> findByUserEmailAndWithdrawnAtIsNull(String userEmail);
}