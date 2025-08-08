package com.boot.loiteBackend.web.support.faq.category.service;

import com.boot.loiteBackend.web.support.faq.category.dto.SupportFaqMajorCategoryDto;
import com.boot.loiteBackend.domain.support.faq.category.entity.SupportFaqMajorCategoryEntity;
import com.boot.loiteBackend.web.support.faq.category.repository.SupportFaqMajorCategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SupportFaqMajorCategoryService {

    private final SupportFaqMajorCategoryRepository majorCategoryRepository;

    public List<SupportFaqMajorCategoryDto> getAllMajorsOrdered() {
        List<SupportFaqMajorCategoryEntity> entities =
                majorCategoryRepository.findAllByOrderByFaqMajorCategoryOrderAsc();

        return entities.stream()
                .map(SupportFaqMajorCategoryDto::from)
                .collect(Collectors.toList());
    }
}

