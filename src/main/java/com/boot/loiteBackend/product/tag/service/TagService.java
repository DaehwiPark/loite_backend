package com.boot.loiteBackend.product.tag.service;

import com.boot.loiteBackend.product.tag.dto.TagRequestDto;
import com.boot.loiteBackend.product.tag.dto.TagResponseDto;

import java.util.List;

public interface TagService {
    void saveTag(List<TagRequestDto> tagRequestDtoList);
    List<TagResponseDto> findAll();
    void deleteTag(Long tagId);
}
