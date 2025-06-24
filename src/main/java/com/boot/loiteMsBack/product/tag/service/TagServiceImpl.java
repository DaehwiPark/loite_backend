package com.boot.loiteMsBack.product.tag.service;

import com.boot.loiteMsBack.product.tag.dto.TagRequestDto;
import com.boot.loiteMsBack.product.tag.dto.TagResponseDto;
import com.boot.loiteMsBack.product.tag.entity.TagEntity;
import com.boot.loiteMsBack.product.tag.mapper.TagMapper;
import com.boot.loiteMsBack.product.tag.repository.ProductTagRepository;
import com.boot.loiteMsBack.product.tag.repository.TagRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class TagServiceImpl implements TagService{
    private final TagRepository tagRepository;
    private final ProductTagRepository productTagRepository;
    private final TagMapper tagMapper;

    @Override
    public void saveTag(List<TagRequestDto> dtoList) {
        List<TagEntity> tags = dtoList.stream().map(tagMapper::toEntity).collect(Collectors.toList());
        tagRepository.saveAll(tags);
    }

    @Override
    public List<TagResponseDto> findAll(){
        return tagRepository.findAll().stream().map(tagMapper::toResponseDto).collect(Collectors.toList());
    }

    @Override
    public void deleteTag(Long tagId) {
        if (!tagRepository.existsById(tagId)) {
            throw new EntityNotFoundException("태그가 존재하지 않습니다.");
        }
        //상품-태그 관계 삭제
        productTagRepository.deleteAllByTag_TagId(tagId);

        //태그 자체 삭제
        tagRepository.deleteById(tagId);
    }
}
