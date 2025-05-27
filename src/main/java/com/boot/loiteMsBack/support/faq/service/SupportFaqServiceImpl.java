package com.boot.loiteMsBack.support.faq.service;

import com.boot.loiteMsBack.support.faq.dto.SupportFaqRequestDto;
import com.boot.loiteMsBack.support.faq.dto.SupportFaqDto;
import com.boot.loiteMsBack.support.faq.entity.SupportFaqEntity;
import com.boot.loiteMsBack.support.faq.repository.SupportFaqRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SupportFaqServiceImpl implements SupportFaqService {

    private final SupportFaqRepository supportFaqRepository;

    public SupportFaqServiceImpl(SupportFaqRepository supportFaqRepository) {
        this.supportFaqRepository = supportFaqRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<SupportFaqDto> getAllFaq() {
        List<SupportFaqEntity> faqs = supportFaqRepository.findAll();
        return faqs.stream()
                .map(SupportFaqDto::new)
                .collect(Collectors.toList());
    }

    @Override
    public SupportFaqDto getFaqById(Long id) {
        SupportFaqEntity entity = supportFaqRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("FAQ not found with id: " + id));
        return new SupportFaqDto(entity);
    }

    @Override
    public SupportFaqDto updateFaq(Long id, SupportFaqRequestDto request) {
        SupportFaqEntity entity = supportFaqRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("FAQ not found with id: " + id));
        entity.setFaqQuestion(request.getFaqQuestion());
        entity.setFaqAnswer(request.getFaqAnswer());
        supportFaqRepository.save(entity);

        return new SupportFaqDto(entity);
    }


    @Override
    @Transactional
    public void deleteFaqById(Long id) {
        if (!supportFaqRepository.existsById(id)) {
            throw new RuntimeException("Cannot delete. FAQ not found with id: " + id);
        }
        supportFaqRepository.deleteById(id);
    }
}