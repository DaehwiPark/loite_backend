package com.boot.loiteMsBack.support.suggestion.repository;

import com.boot.loiteMsBack.support.suggestion.entity.SupportSuggestionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SupportSuggestionRepository extends JpaRepository<SupportSuggestionEntity, Long> {
    List<SupportSuggestionEntity> findByDelYn(String delYn);
    Optional<SupportSuggestionEntity> findBySuggestionIdAndDelYn(Long suggestionId, String delYn); // 수정된 메서드
}
