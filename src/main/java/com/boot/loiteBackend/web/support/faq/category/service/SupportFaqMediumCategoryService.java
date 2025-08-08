package com.boot.loiteBackend.web.support.faq.category.service;

import com.boot.loiteBackend.web.support.faq.category.dto.SupportFaqMediumCategoryDto;
import com.boot.loiteBackend.domain.support.faq.category.entity.SupportFaqMediumCategoryEntity;
import com.boot.loiteBackend.web.support.faq.category.repository.SupportFaqMediumCategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SupportFaqMediumCategoryService {

    private final SupportFaqMediumCategoryRepository mediumCategoryRepository;

    public List<SupportFaqMediumCategoryDto> getMediumsByMajorId(Long majorCategoryId) {
        List<SupportFaqMediumCategoryEntity> entities =
                mediumCategoryRepository.findAllByFaqMajorCategory_FaqMajorCategoryIdOrderByFaqMediumCategoryOrderAsc(majorCategoryId);

        return entities.stream()
                .map(SupportFaqMediumCategoryDto::from)
                .collect(Collectors.toList());
    }
}
