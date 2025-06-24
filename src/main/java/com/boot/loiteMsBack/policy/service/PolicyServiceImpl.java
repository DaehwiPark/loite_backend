package com.boot.loiteMsBack.policy.service;

import com.boot.loiteMsBack.policy.dto.PolicyDto;
import com.boot.loiteMsBack.policy.entity.PolicyEntity;
import com.boot.loiteMsBack.policy.error.PolicyErrorCode;
import com.boot.loiteMsBack.policy.repository.PolicyRepository;
import com.boot.loiteMsBack.global.error.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Policy;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class PolicyServiceImpl implements PolicyService {

    private final PolicyRepository policyRepository;

    @Override
    @Transactional
    public PolicyDto create(PolicyDto dto) {
        try {
            PolicyEntity entity = dto.toEntity();
            return PolicyDto.fromEntity(policyRepository.save(entity));
        } catch (Exception e) {
            throw new CustomException(PolicyErrorCode.SAVE_FAILED);
        }
    }

    @Override
    @Transactional
    public PolicyDto update(Long id, PolicyDto dto) {
        PolicyEntity entity = policyRepository.findById(id)
                .orElseThrow(() -> new CustomException(PolicyErrorCode.NOT_FOUND));

        entity.setPolicyTitle(dto.getPolicyTitle());
        entity.setPolicyContent(dto.getPolicyContent());
        entity.setDisplayYn(dto.getDisplayYn());

        return PolicyDto.fromEntity(policyRepository.save(entity));
    }

    @Override
    @Transactional
    public void delete(Long id) {
        if (!policyRepository.existsById(id)) {
            throw new CustomException(PolicyErrorCode.NOT_FOUND);
        }
        policyRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public PolicyDto getById(Long id) {
        return policyRepository.findById(id)
                .map(PolicyDto::fromEntity)
                .orElseThrow(() -> new CustomException(PolicyErrorCode.NOT_FOUND));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PolicyDto> getPagedPolicy(String keyword, Pageable pageable) {
        Page<PolicyEntity> policyPage = policyRepository.findByKeyword(keyword, pageable);
        return policyPage.map(PolicyDto::fromEntity);
    }

}
