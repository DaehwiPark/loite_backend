package com.boot.loiteMsBack.support.service;


import com.boot.loiteMsBack.support.dto.SupportFaqDto;
import com.boot.loiteMsBack.support.entity.SupportFaqEntity;
import com.boot.loiteMsBack.support.repository.SupportRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SupportServiceImpl implements SupportService {

    private final SupportRepository supportRepository;

    public SupportServiceImpl(SupportRepository supportRepository) {
        this.supportRepository = supportRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<SupportFaqDto> getAllFaqDtos() {
        List<SupportFaqEntity> faqs = supportRepository.findAll();
        return faqs.stream()
                .map(SupportFaqDto::new)
                .collect(Collectors.toList());
    }

    @Override
    public SupportFaqDto getFaqDtoById(Long id) {
        SupportFaqEntity entity = supportRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("FAQ not found with id: " + id));
        return new SupportFaqDto(entity);
    }
    @Override
    @Transactional(readOnly = true)
    public List<SupportFaqDto> getUnansweredFaqDtos() {
        List<SupportFaqEntity> faqs = supportRepository.findAll();
        return faqs.stream()
                .filter(faq -> faq.getFaqContent() == null || faq.getFaqContent().isBlank())
                .map(SupportFaqDto::new)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public SupportFaqDto addAnswerToFaq(Long id, String answerContent) {
        SupportFaqEntity entity = supportRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("FAQ not found with id: " + id));

        if (entity.getFaqContent() != null && !entity.getFaqContent().isBlank()) {
            throw new IllegalStateException("FAQ already has an answer. Use update instead.");
        }

        entity.setFaqContent(answerContent);
        supportRepository.save(entity);

        return new SupportFaqDto(entity);
    }

    @Override
    @Transactional
    public SupportFaqDto updateFaqAnswer(Long id, String updatedContent) {
        SupportFaqEntity entity = supportRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("FAQ not found with id: " + id));

        entity.setFaqContent(updatedContent);
        supportRepository.save(entity);

        return new SupportFaqDto(entity);
    }

    @Override
    @Transactional
    public void deleteFaqById(Long id) {
        if (!supportRepository.existsById(id)) {
            throw new RuntimeException("Cannot delete. FAQ not found with id: " + id);
        }
        supportRepository.deleteById(id);
    }



}
