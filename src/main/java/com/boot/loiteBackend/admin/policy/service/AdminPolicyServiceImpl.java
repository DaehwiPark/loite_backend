package com.boot.loiteBackend.admin.policy.service;

import com.boot.loiteBackend.admin.policy.dto.AdminPolicyDto;
import com.boot.loiteBackend.admin.policy.entity.AdminPolicyEntity;
import com.boot.loiteBackend.admin.policy.error.AdminPolicyErrorCode;
import com.boot.loiteBackend.admin.policy.repository.AdminPolicyRepository;
import com.boot.loiteBackend.global.error.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class AdminPolicyServiceImpl implements AdminPolicyService {

    private final AdminPolicyRepository adminPolicyRepository;

    @Override
    @Transactional
    public AdminPolicyDto create(AdminPolicyDto dto) {
        try {
            AdminPolicyEntity entity = dto.toEntity();
            return AdminPolicyDto.fromEntity(adminPolicyRepository.save(entity));
        } catch (Exception e) {
            throw new CustomException(AdminPolicyErrorCode.SAVE_FAILED);
        }
    }

    @Override
    @Transactional
    public AdminPolicyDto update(Long id, AdminPolicyDto dto) {
        AdminPolicyEntity entity = adminPolicyRepository.findById(id)
                .orElseThrow(() -> new CustomException(AdminPolicyErrorCode.NOT_FOUND));

        entity.setPolicyTitle(dto.getPolicyTitle());
        entity.setPolicyContent(dto.getPolicyContent());
        entity.setDisplayYn(dto.getDisplayYn());

        return AdminPolicyDto.fromEntity(adminPolicyRepository.save(entity));
    }

    @Override
    @Transactional
    public void delete(Long id) {
        if (!adminPolicyRepository.existsById(id)) {
            throw new CustomException(AdminPolicyErrorCode.NOT_FOUND);
        }
        adminPolicyRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public AdminPolicyDto getById(Long id) {
        return adminPolicyRepository.findById(id)
                .map(AdminPolicyDto::fromEntity)
                .orElseThrow(() -> new CustomException(AdminPolicyErrorCode.NOT_FOUND));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AdminPolicyDto> getPagedPolicy(String keyword, Pageable pageable) {
        Page<AdminPolicyEntity> policyPage = adminPolicyRepository.findByKeyword(keyword, pageable);
        return policyPage.map(AdminPolicyDto::fromEntity);
    }

}
