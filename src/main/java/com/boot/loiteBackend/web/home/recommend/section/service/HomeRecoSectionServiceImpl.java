package com.boot.loiteBackend.web.home.recommend.section.service;

import com.boot.loiteBackend.web.home.recommend.section.dto.HomeRecoSectionResponseDto;
import com.boot.loiteBackend.web.home.recommend.section.repository.HomeRecoSectionRepository;
import com.boot.loiteBackend.web.home.recommend.section.service.HomeRecoSectionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class HomeRecoSectionServiceImpl implements HomeRecoSectionService {

    private final HomeRecoSectionRepository homeRecoSectionRepository;

    @Override
    @Transactional(readOnly = true)
    public List<HomeRecoSectionResponseDto> list() {
        var entities = homeRecoSectionRepository.findAll();
        return entities.stream()
                .map(HomeRecoSectionResponseDto::fromEntity)
                .toList();
    }
}
