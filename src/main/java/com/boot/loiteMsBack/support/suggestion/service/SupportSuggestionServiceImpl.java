package com.boot.loiteMsBack.support.suggestion.service;

import com.boot.loiteMsBack.global.error.exception.CustomException;
import com.boot.loiteMsBack.support.suggestion.dto.SupportSuggestionDto;
import com.boot.loiteMsBack.support.suggestion.entity.SupportSuggestionEntity;
import com.boot.loiteMsBack.support.suggestion.error.SuggestionErrorCode;
import com.boot.loiteMsBack.support.suggestion.mapper.SupportSuggestionMapper;
import com.boot.loiteMsBack.support.suggestion.repository.SupportSuggestionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SupportSuggestionServiceImpl implements SupportSuggestionService {

    private final SupportSuggestionRepository suggestionRepository;
    private final SupportSuggestionMapper supportSuggestionMapper;

    @Override
    @Transactional(readOnly = true)
    public List<SupportSuggestionDto> getAllSuggestions() {
        return suggestionRepository.findByDelYn("N").stream()
                .map(supportSuggestionMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public SupportSuggestionDto getSuggestionById(Long id) {
        SupportSuggestionEntity entity = getEntityOrThrow(id);
        return supportSuggestionMapper.toDto(entity);
    }

    @Override
    @Transactional
    public void deleteSuggestion(Long id) {
        SupportSuggestionEntity entity = getEntityOrThrow(id);
        try {
            entity.setDelYn("Y");
            suggestionRepository.save(entity);
        } catch (Exception e) {
            throw new CustomException(SuggestionErrorCode.DELETE_FAILED);
        }
    }

    // 중복 제거를 위한 공통 메서드
    private SupportSuggestionEntity getEntityOrThrow(Long id) {
        return suggestionRepository.findBySuggestionIdAndDelYn(id, "N")
                .orElseThrow(() -> new CustomException(SuggestionErrorCode.NOT_FOUND));
    }
}
