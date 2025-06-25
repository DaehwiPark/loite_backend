package com.boot.loiteBackend.admin.support.suggestion.general.repository;

import com.boot.loiteBackend.admin.support.suggestion.general.entity.AdminSupportSuggestionEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface AdminSupportSuggestionRepository extends JpaRepository<AdminSupportSuggestionEntity, Long> {
    @Query("SELECT s FROM AdminSupportSuggestionEntity s " +
            "WHERE s.deleteYn = :deleteYn " +
            "AND (s.suggestionTitle LIKE %:keyword% OR s.suggestionContent LIKE %:keyword%)")
    Page<AdminSupportSuggestionEntity> findByKeywordAndDeleteYn(@Param("keyword") String keyword,
                                                                @Param("deleteYn") String deleteYn,
                                                                Pageable pageable);
    Page<AdminSupportSuggestionEntity> findByDeleteYn(String deleteYn, Pageable pageable);
    Optional<AdminSupportSuggestionEntity> findBySuggestionIdAndDeleteYn(Long suggestionId, String deleteYn);
}
