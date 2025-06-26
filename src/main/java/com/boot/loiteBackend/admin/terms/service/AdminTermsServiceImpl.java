package com.boot.loiteBackend.admin.terms.service;

import com.boot.loiteBackend.global.error.exception.CustomException;
import com.boot.loiteBackend.admin.terms.dto.AdminTermsDto;
import com.boot.loiteBackend.admin.terms.entity.AdminTermsEntity;
import com.boot.loiteBackend.admin.terms.error.AdminTermsErrorCode;
import com.boot.loiteBackend.admin.terms.repository.AdminTermsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class AdminTermsServiceImpl implements AdminTermsService {

    private final AdminTermsRepository adminTermsRepository;

    @Override
    @Transactional
    public AdminTermsDto createTerms(AdminTermsDto dto) {
        try {
            AdminTermsEntity saved = adminTermsRepository.save(dto.toEntity());
            return AdminTermsDto.fromEntity(saved);
        } catch (Exception e) {
            throw new CustomException(AdminTermsErrorCode.SAVE_FAILED);
        }
    }

    @Override
    @Transactional
    public AdminTermsDto updateTerms(Long id, AdminTermsDto dto) {
        AdminTermsEntity entity = adminTermsRepository.findById(id)
                .orElseThrow(() -> new CustomException(AdminTermsErrorCode.NOT_FOUND));

        entity.setTermsTitle(dto.getTermsTitle());
        entity.setTermsContent(dto.getTermsContent());

        AdminTermsEntity updated = adminTermsRepository.save(entity);
        return AdminTermsDto.fromEntity(updated);
    }

    @Override
    @Transactional
    public void deleteTerms(Long id) {
        if (!adminTermsRepository.existsById(id)) {
            throw new CustomException(AdminTermsErrorCode.DELETE_FAILED);
        }
        adminTermsRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AdminTermsDto> getPagedTerms(String keyword, Pageable pageable) {
        Page<AdminTermsEntity> termsPage = adminTermsRepository.findByKeyword(keyword, pageable);
        return termsPage.map(AdminTermsDto::fromEntity);
    }


    @Override
    @Transactional(readOnly = true)
    public AdminTermsDto getTermsById(Long id) {
        AdminTermsEntity entity = adminTermsRepository.findById(id)
                .orElseThrow(() -> new CustomException(AdminTermsErrorCode.NOT_FOUND));
        return AdminTermsDto.fromEntity(entity);
    }
}
