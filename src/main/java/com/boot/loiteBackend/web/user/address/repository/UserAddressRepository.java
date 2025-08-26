package com.boot.loiteBackend.web.user.address.repository;

import com.boot.loiteBackend.domain.user.address.entity.UserAddressEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import jakarta.persistence.LockModeType;
import java.util.Optional;

public interface UserAddressRepository extends JpaRepository<UserAddressEntity, Long> {

    /**
     * 검색/페이징: q가 비어있으면 전체, 있으면 alias/수신인/우편번호/주소1/주소2 부분일치(대소문자 무시)
     * 삭제되지 않은 건만 조회(DELETE_YN = 'N')
     */
    @Query(value = """
           SELECT a
             FROM UserAddressEntity a
            WHERE a.userId = :userId
              AND a.deleteYn = 'N'
              AND (
                    :q IS NULL OR :q = '' OR
                    LOWER(COALESCE(a.alias, ''))        LIKE CONCAT('%', LOWER(:q), '%') OR
                    LOWER(COALESCE(a.receiverName, '')) LIKE CONCAT('%', LOWER(:q), '%') OR
                    LOWER(COALESCE(a.zipCode, ''))      LIKE CONCAT('%', LOWER(:q), '%') OR
                    LOWER(COALESCE(a.addressLine1, '')) LIKE CONCAT('%', LOWER(:q), '%') OR
                    LOWER(COALESCE(a.addressLine2, '')) LIKE CONCAT('%', LOWER(:q), '%')
                  )
           """,
            countQuery = """
           SELECT COUNT(a)
             FROM UserAddressEntity a
            WHERE a.userId = :userId
              AND a.deleteYn = 'N'
              AND (
                    :q IS NULL OR :q = '' OR
                    LOWER(COALESCE(a.alias, ''))        LIKE CONCAT('%', LOWER(:q), '%') OR
                    LOWER(COALESCE(a.receiverName, '')) LIKE CONCAT('%', LOWER(:q), '%') OR
                    LOWER(COALESCE(a.zipCode, ''))      LIKE CONCAT('%', LOWER(:q), '%') OR
                    LOWER(COALESCE(a.addressLine1, '')) LIKE CONCAT('%', LOWER(:q), '%') OR
                    LOWER(COALESCE(a.addressLine2, '')) LIKE CONCAT('%', LOWER(:q), '%')
                  )
           """)
    Page<UserAddressEntity> search(@Param("userId") Long userId,
                                   @Param("q") String q,
                                   Pageable pageable);

    /* 단건 조회(삭제되지 않은 건만) */
    Optional<UserAddressEntity> findByIdAndUserIdAndDeleteYn(Long id, Long userId, String deleteYn);

    /* 기본 배송지 조회(삭제되지 않은 건만) */
    Optional<UserAddressEntity> findByUserIdAndDefaultYnAndDeleteYn(Long userId, String defaultYn, String deleteYn);

    /**
     * 유저의 기존 기본 배송지 플래그 일괄 해제 (현재 주소 제외)
     * - 삭제되지 않은 건만 대상으로 DEFAULT_YN = 'N'
     * - clearAutomatically=false 로 영속성 컨텍스트를 유지하여 더티체킹이 이어지도록 함
     */
    @Modifying(clearAutomatically = false, flushAutomatically = true)
    @Query("""
           UPDATE UserAddressEntity a
              SET a.defaultYn = 'N'
            WHERE a.userId = :userId
              AND a.deleteYn = 'N'
              AND a.defaultYn = 'Y'
              AND a.id <> :addressId
           """)
    int resetDefaultForUserExcept(@Param("userId") Long userId, @Param("addressId") Long addressId);

    /**
     * (선택) 모든 기본 플래그 해제 - 필요 시 사용
     * 주의: 이 메서드는 현재 주소까지 N으로 만들 수 있으므로 일반 업데이트 흐름에서는 사용하지 않는 것을 권장
     */
    @Modifying(clearAutomatically = false, flushAutomatically = true)
    @Query("""
           UPDATE UserAddressEntity a
              SET a.defaultYn = 'N'
            WHERE a.userId = :userId
              AND a.deleteYn = 'N'
              AND a.defaultYn = 'Y'
           """)
    int resetDefaultForUser(@Param("userId") Long userId);

    /**
     * 소프트 삭제: DELETE_YN='Y', 기본 플래그도 함께 해제(DEFAULT_YN='N')
     * - clearAutomatically=false: 현재 트랜잭션의 영속성 컨텍스트를 유지
     */
    @Modifying(clearAutomatically = false, flushAutomatically = true)
    @Query("""
           UPDATE UserAddressEntity a
              SET a.deleteYn = 'Y',
                  a.defaultYn = 'N'
            WHERE a.id = :id
              AND a.userId = :userId
              AND a.deleteYn = 'N'
           """)
    int softDelete(@Param("id") Long id, @Param("userId") Long userId);

    /**
     * 동시성 제어: 현재 기본 배송지 레코드를 잠금(PESSIMISTIC_WRITE)
     * - 트랜잭션 내 동시 기본지정 경쟁을 줄이고 싶을 때 사용 가능
     */
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("""
           SELECT a
             FROM UserAddressEntity a
            WHERE a.userId = :userId
              AND a.defaultYn = 'Y'
              AND a.deleteYn = 'N'
           """)
    Optional<UserAddressEntity> lockDefaultForUpdate(@Param("userId") Long userId);
}