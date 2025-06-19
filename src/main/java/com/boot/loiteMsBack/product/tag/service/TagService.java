package com.boot.loiteMsBack.product.tag.service;

import com.boot.loiteMsBack.product.tag.dto.TagRequestDto;
import com.boot.loiteMsBack.product.tag.dto.TagResponseDto;

import java.util.List;

public interface TagService {
    void saveTag(List<TagRequestDto> tagRequestDtoList);
    List<TagResponseDto> findAll();
}
