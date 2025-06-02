package com.boot.loiteMsBack.policy.service;

import com.boot.loiteMsBack.policy.dto.PolicyDto;
import com.boot.loiteMsBack.policy.entity.PolicyEntity;
import com.boot.loiteMsBack.policy.error.PolicyErrorCode;
import com.boot.loiteMsBack.policy.repository.PolicyRepository;
import com.boot.loiteMsBack.global.error.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

        entity.setTitle(dto.getTitle());
        entity.setContent(dto.getContent());
        entity.setVersion(dto.getVersion());
        entity.setIsActive(dto.getIsActive());

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
    public List<PolicyDto> getAll() {
        return policyRepository.findAll()
                .stream()
                .map(PolicyDto::fromEntity)
                .collect(Collectors.toList());
    }
}
