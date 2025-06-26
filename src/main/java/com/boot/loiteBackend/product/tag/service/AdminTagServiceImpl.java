package com.boot.loiteBackend.product.tag.service;

import com.boot.loiteBackend.product.tag.dto.AdminTagRequestDto;
import com.boot.loiteBackend.product.tag.dto.AdminTagResponseDto;
import com.boot.loiteBackend.product.tag.entity.AdminTagEntity;
import com.boot.loiteBackend.product.tag.mapper.AdminTagMapper;
import com.boot.loiteBackend.product.tag.repository.AdminProductTagRepository;
import com.boot.loiteBackend.product.tag.repository.AdminTagRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class AdminTagServiceImpl implements AdminTagService {
    private final AdminTagRepository adminTagRepository;
    private final AdminProductTagRepository adminProductTagRepository;
    private final AdminTagMapper adminTagMapper;

    @Override
    public void saveTag(List<AdminTagRequestDto> dtoList) {
        List<AdminTagEntity> tags = dtoList.stream().map(adminTagMapper::toEntity).collect(Collectors.toList());
        adminTagRepository.saveAll(tags);
    }

    @Override
    public List<AdminTagResponseDto> findAll(){
        return adminTagRepository.findAll().stream().map(adminTagMapper::toResponseDto).collect(Collectors.toList());
    }

    @Override
    public void deleteTag(Long tagId) {
        if (!adminTagRepository.existsById(tagId)) {
            throw new EntityNotFoundException("태그가 존재하지 않습니다.");
        }
        //상품-태그 관계 삭제
        adminProductTagRepository.deleteAllByTag_TagId(tagId);

        //태그 자체 삭제
        adminTagRepository.deleteById(tagId);
    }
}
