package com.boot.loiteBackend.web.support.resource.category.service;

import com.boot.loiteBackend.web.support.resource.category.dto.SupportResourceCategoryDto;
import com.boot.loiteBackend.web.support.resource.category.repository.SupportResourceCategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SupportResourceCategoryService {

    private final SupportResourceCategoryRepository supportResourceCategoryRepository;

    public List<SupportResourceCategoryDto> getUsableCategories() {
        return supportResourceCategoryRepository.findValidCategoriesHavingManuals()
                .stream()
                .map(SupportResourceCategoryDto::from)
                .collect(Collectors.toList());
    }
}
