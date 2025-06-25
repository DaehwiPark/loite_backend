package com.boot.loiteBackend.support.suggestion.file.repository;

import com.boot.loiteBackend.support.suggestion.file.entity.SupportSuggestionFileEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SupportSuggestionFileRepository extends JpaRepository<SupportSuggestionFileEntity, Long> {

    List<SupportSuggestionFileEntity> findBySuggestionId(Long suggestionId);
    void deleteBySuggestionId(Long suggestionId); // 필요 시
}
