package com.boot.loiteMsBack.support.suggestion.general.repository;

import com.boot.loiteMsBack.support.suggestion.general.entity.SupportSuggestionEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface SupportSuggestionRepository extends JpaRepository<SupportSuggestionEntity, Long> {
    @Query("SELECT s FROM SupportSuggestionEntity s " +
            "WHERE s.delYn = :delYn " +
            "AND (s.suggestionTitle LIKE %:keyword% OR s.suggestionContent LIKE %:keyword%)")
    Page<SupportSuggestionEntity> findByKeywordAndDelYn(@Param("keyword") String keyword,
                                                        @Param("delYn") String delYn,
                                                        Pageable pageable);
    Page<SupportSuggestionEntity> findByDelYn(String delYn, Pageable pageable);
    Optional<SupportSuggestionEntity> findBySuggestionIdAndDelYn(Long suggestionId, String delYn);
}
