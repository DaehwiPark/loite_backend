package com.boot.loiteBackend.product.tag.service;

import com.boot.loiteBackend.product.tag.dto.AdminTagRequestDto;
import com.boot.loiteBackend.product.tag.dto.AdminTagResponseDto;

import java.util.List;

public interface AdminTagService {
    void saveTag(List<AdminTagRequestDto> adminTagRequestDtoList);
    List<AdminTagResponseDto> findAll();
    void deleteTag(Long tagId);
}
