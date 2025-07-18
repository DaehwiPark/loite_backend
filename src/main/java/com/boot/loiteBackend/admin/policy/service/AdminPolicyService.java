package com.boot.loiteBackend.admin.policy.service;

import com.boot.loiteBackend.admin.policy.dto.AdminPolicyDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AdminPolicyService {
    AdminPolicyDto create(AdminPolicyDto dto);
    AdminPolicyDto update(Long id, AdminPolicyDto dto);
    void delete(Long id);
    AdminPolicyDto getById(Long id);
    Page<AdminPolicyDto> getPagedPolicy(String Keyword, Pageable pageable);
}
