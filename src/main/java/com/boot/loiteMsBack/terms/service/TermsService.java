package com.boot.loiteMsBack.terms.service;

import com.boot.loiteMsBack.terms.dto.TermsDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface TermsService {

    TermsDto createTerms(TermsDto dto);

    TermsDto updateTerms(Long id, TermsDto dto);

    void deleteTerms(Long id);

    Page<TermsDto> getPagedTerms(String keyword, Pageable pageable);

    TermsDto getTermsById(Long id);
}
