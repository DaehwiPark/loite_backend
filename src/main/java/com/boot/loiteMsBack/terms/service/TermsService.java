package com.boot.loiteMsBack.terms.service;

import com.boot.loiteMsBack.terms.dto.TermsDto;

import java.util.List;

public interface TermsService {

    TermsDto createTerms(TermsDto dto);

    TermsDto updateTerms(Long id, TermsDto dto);

    void deleteTerms(Long id);

    List<TermsDto> getAllTerms();

    TermsDto getTermsById(Long id);
}
