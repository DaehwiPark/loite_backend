package com.boot.loiteBackend.policy.service;

import com.boot.loiteBackend.policy.dto.PolicyDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PolicyService {
    PolicyDto create(PolicyDto dto);
    PolicyDto update(Long id, PolicyDto dto);
    void delete(Long id);
    PolicyDto getById(Long id);
    Page<PolicyDto> getPagedPolicy(String Keyword, Pageable pageable);
}
