package com.boot.loiteBackend.admin.terms.service;

import com.boot.loiteBackend.admin.terms.dto.AdminTermsDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AdminTermsService {

    AdminTermsDto createTerms(AdminTermsDto dto);

    AdminTermsDto updateTerms(Long id, AdminTermsDto dto);

    void deleteTerms(Long id);

    Page<AdminTermsDto> getPagedTerms(String keyword, Pageable pageable);

    AdminTermsDto getTermsById(Long id);
}
