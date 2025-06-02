package com.boot.loiteMsBack.policy.service;

import com.boot.loiteMsBack.policy.dto.PolicyDto;

import java.util.List;

public interface PolicyService {
    PolicyDto create(PolicyDto dto);
    PolicyDto update(Long id, PolicyDto dto);
    void delete(Long id);
    PolicyDto getById(Long id);
    List<PolicyDto> getAll();
}
