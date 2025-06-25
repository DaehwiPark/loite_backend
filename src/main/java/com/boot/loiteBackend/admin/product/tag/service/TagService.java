package com.boot.loiteBackend.admin.product.tag.service;

import com.boot.loiteBackend.admin.product.tag.dto.TagRequestDto;
import com.boot.loiteBackend.admin.product.tag.dto.TagResponseDto;

import java.util.List;

public interface TagService {
    void saveTag(List<TagRequestDto> tagRequestDtoList);
    List<TagResponseDto> findAll();
    void deleteTag(Long tagId);
}
