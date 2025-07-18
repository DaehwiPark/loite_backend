package com.boot.loiteBackend.admin.support.suggestion.file.repository;

import com.boot.loiteBackend.admin.support.suggestion.file.entity.AdminSupportSuggestionFileEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AdminSupportSuggestionFileRepository extends JpaRepository<AdminSupportSuggestionFileEntity, Long> {

    List<AdminSupportSuggestionFileEntity> findBySuggestionId(Long suggestionId);
    void deleteBySuggestionId(Long suggestionId); // 필요 시
}
