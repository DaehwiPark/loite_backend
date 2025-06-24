package com.boot.loiteMsBack.terms.service;

import com.boot.loiteMsBack.global.error.exception.CustomException;
import com.boot.loiteMsBack.terms.dto.TermsDto;
import com.boot.loiteMsBack.terms.entity.TermsEntity;
import com.boot.loiteMsBack.terms.error.TermsErrorCode;
import com.boot.loiteMsBack.terms.repository.TermsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class TermsServiceImpl implements TermsService {

    private final TermsRepository termsRepository;

    @Override
    @Transactional
    public TermsDto createTerms(TermsDto dto) {
        try {
            TermsEntity saved = termsRepository.save(dto.toEntity());
            return TermsDto.fromEntity(saved);
        } catch (Exception e) {
            throw new CustomException(TermsErrorCode.SAVE_FAILED);
        }
    }

    @Override
    @Transactional
    public TermsDto updateTerms(Long id, TermsDto dto) {
        TermsEntity entity = termsRepository.findById(id)
                .orElseThrow(() -> new CustomException(TermsErrorCode.NOT_FOUND));

        entity.setTermsTitle(dto.getTermsTitle());
        entity.setTermsContent(dto.getTermsContent());

        TermsEntity updated = termsRepository.save(entity);
        return TermsDto.fromEntity(updated);
    }

    @Override
    @Transactional
    public void deleteTerms(Long id) {
        if (!termsRepository.existsById(id)) {
            throw new CustomException(TermsErrorCode.DELETE_FAILED);
        }
        termsRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<TermsDto> getPagedTerms(String keyword, Pageable pageable) {
        Page<TermsEntity> termsPage = termsRepository.findByKeyword(keyword, pageable);
        return termsPage.map(TermsDto::fromEntity);
    }


    @Override
    @Transactional(readOnly = true)
    public TermsDto getTermsById(Long id) {
        TermsEntity entity = termsRepository.findById(id)
                .orElseThrow(() -> new CustomException(TermsErrorCode.NOT_FOUND));
        return TermsDto.fromEntity(entity);
    }
}
