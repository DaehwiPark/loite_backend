package com.boot.loiteBackend.web.user.address.repository;

import com.boot.loiteBackend.domain.useraddress.entity.UserAddressEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import jakarta.persistence.LockModeType;
import java.util.Optional;

public interface UserAddressRepository extends JpaRepository<UserAddressEntity, Long> {

    /**
     * 검색/페이징: q가 비어있으면 전체, 있으면 alias/수신인/우편번호/주소1/주소2에 부분일치(대소문자 무시)
     * countQuery를 별도 지정하여 페이징 성능/정확도 개선
     */
    @Query(value = """
           SELECT a FROM UserAddressEntity a
           WHERE a.userId = :userId
             AND a.isDeleted = false
             AND (
                 :q IS NULL OR :q = '' OR
                 LOWER(COALESCE(a.alias, ''))         LIKE CONCAT('%', LOWER(:q), '%') OR
                 LOWER(COALESCE(a.recipientName, ''))  LIKE CONCAT('%', LOWER(:q), '%') OR
                 LOWER(COALESCE(a.zipCode, ''))        LIKE CONCAT('%', LOWER(:q), '%') OR
                 LOWER(COALESCE(a.addressLine1, ''))   LIKE CONCAT('%', LOWER(:q), '%') OR
                 LOWER(COALESCE(a.addressLine2, ''))   LIKE CONCAT('%', LOWER(:q), '%')
             )
           """,
            countQuery = """
           SELECT COUNT(a) FROM UserAddressEntity a
           WHERE a.userId = :userId
             AND a.isDeleted = false
             AND (
                 :q IS NULL OR :q = '' OR
                 LOWER(COALESCE(a.alias, ''))         LIKE CONCAT('%', LOWER(:q), '%') OR
                 LOWER(COALESCE(a.recipientName, ''))  LIKE CONCAT('%', LOWER(:q), '%') OR
                 LOWER(COALESCE(a.zipCode, ''))        LIKE CONCAT('%', LOWER(:q), '%') OR
                 LOWER(COALESCE(a.addressLine1, ''))   LIKE CONCAT('%', LOWER(:q), '%') OR
                 LOWER(COALESCE(a.addressLine2, ''))   LIKE CONCAT('%', LOWER(:q), '%')
             )
           """)
    Page<UserAddressEntity> search(@Param("userId") Long userId,
                                   @Param("q") String q,
                                   Pageable pageable);

    Optional<UserAddressEntity> findByIdAndUserIdAndIsDeletedFalse(Long id, Long userId);

    Optional<UserAddressEntity> findByUserIdAndIsDefaultTrueAndIsDeletedFalse(Long userId);

    boolean existsByUserIdAndIsDefaultTrueAndIsDeletedFalse(Long userId);

    /**
     * 유저의 기존 기본 배송지 플래그를 일괄 해제
     */
    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("UPDATE UserAddressEntity a " +
            "SET a.isDefault = false " +
            "WHERE a.userId = :userId AND a.isDefault = true AND a.isDeleted = false")
    int resetDefaultForUser(@Param("userId") Long userId);

    /**
     * 소프트 삭제: isDeleted=true, 기본 플래그도 함께 해제
     */
    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("UPDATE UserAddressEntity a " +
            "SET a.isDeleted = true, a.isDefault = false " +
            "WHERE a.id = :id AND a.userId = :userId AND a.isDeleted = false")
    int softDelete(@Param("id") Long id, @Param("userId") Long userId);

    /**
     * (선택) 동시성 제어용: 기본 배송지를 갱신하기 직전에 현재 기본건을 잠금
     * setDefault 로직에서 사용하려면 트랜잭션 범위 내에서 호출하세요.
     */
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT a FROM UserAddressEntity a " +
            "WHERE a.userId = :userId AND a.isDefault = true AND a.isDeleted = false")
    Optional<UserAddressEntity> lockDefaultForUpdate(@Param("userId") Long userId);
}
