package com.boot.loiteMsBack.support.suggestion.general.service;

import com.boot.loiteMsBack.global.error.exception.CustomException;
import com.boot.loiteMsBack.support.suggestion.file.dto.SupportSuggestionFileDto;
import com.boot.loiteMsBack.support.suggestion.file.dto.SupportSuggestionFileSummaryDto;
import com.boot.loiteMsBack.support.suggestion.file.entity.SupportSuggestionFileEntity;
import com.boot.loiteMsBack.support.suggestion.file.repository.SupportSuggestionFileRepository;
import com.boot.loiteMsBack.support.suggestion.general.dto.SupportSuggestionDto;
import com.boot.loiteMsBack.support.suggestion.general.dto.SupportSuggestionUpdateDto;
import com.boot.loiteMsBack.support.suggestion.general.entity.SupportSuggestionEntity;
import com.boot.loiteMsBack.support.suggestion.general.error.SuggestionErrorCode;
import com.boot.loiteMsBack.support.suggestion.general.mapper.SupportSuggestionMapper;
import com.boot.loiteMsBack.support.suggestion.general.repository.SupportSuggestionRepository;
import com.boot.loiteMsBack.user.dto.UserDto;
import com.boot.loiteMsBack.user.dto.UserSummaryDto;
import com.boot.loiteMsBack.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SupportSuggestionServiceImpl implements SupportSuggestionService {

    private final SupportSuggestionRepository suggestionRepository;
    private final SupportSuggestionMapper supportSuggestionMapper;
    private final SupportSuggestionFileRepository suggestionFileRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional(readOnly = true)
    public Page<SupportSuggestionDto> getPagedSuggestions(String keyword, Pageable pageable) {
        Page<SupportSuggestionEntity> page;

        if (StringUtils.hasText(keyword)) {
            page = suggestionRepository.findByKeywordAndDeleteYn(keyword, "N", pageable);
        } else {
            page = suggestionRepository.findByDeleteYn("N", pageable);
        }

        return page.map(entity -> {
            SupportSuggestionDto dto = supportSuggestionMapper.toDto(entity);

            // 사용자 정보 주입
            if (entity.getSuggestionUserId() != null) {
                UserSummaryDto userDto = userRepository.findById(entity.getSuggestionUserId())
                        .map(user -> UserSummaryDto.builder()
                                .userId(user.getUserId())
                                .name(user.getName())
                                .build())
                        .orElse(null);
                dto.setUserSummaryDto(userDto);
            }

            return dto;
        });
    }


    @Override
    @Transactional(readOnly = true)
    public SupportSuggestionDto getSuggestionById(Long id) {
        SupportSuggestionEntity entity = getEntityOrThrow(id);
        SupportSuggestionDto dto = supportSuggestionMapper.toDto(entity);

        // 첨부파일 정보 주입
        List<SupportSuggestionFileSummaryDto> fileDtos = suggestionFileRepository.findBySuggestionId(id)
                .stream()
                .map(file -> SupportSuggestionFileSummaryDto.builder()
                        .suggestionFileId(file.getSuggestionFileId())
                        .suggestionId(file.getSuggestionId())
                        .suggestionFileName(file.getSuggestionFileName())
                        .suggestionFileUrl(file.getSuggestionFileUrl())
                        .suggestionFileType(file.getSuggestionFileType())
                        .build())
                .collect(Collectors.toList());
        dto.setFilesSummaryDto(fileDtos);

        // 사용자 정보 주입
        if (entity.getSuggestionUserId() != null) {
            UserSummaryDto userDto = userRepository.findById(entity.getSuggestionUserId())
                    .map(user -> UserSummaryDto.builder()
                            .userId(user.getUserId())
                            .userEmail(user.getUserEmail())
                            .name(user.getName())
                            .role(user.getRole())
                            .status(user.getStatus())
                            .createdAt(user.getCreatedAt())
                            .build())
                    .orElse(null);
            dto.setUserSummaryDto(userDto);
        }

        return dto;
    }

    @Override
    @Transactional
    public void deleteSuggestion(Long id) {
        SupportSuggestionEntity entity = getEntityOrThrow(id);
        try {
            entity.setDeleteYn("Y");
            suggestionRepository.save(entity);
        } catch (Exception e) {
            throw new CustomException(SuggestionErrorCode.DELETE_FAILED);
        }
    }

    private SupportSuggestionEntity getEntityOrThrow(Long id) {
        return suggestionRepository.findBySuggestionIdAndDeleteYn(id, "N")
                .orElseThrow(() -> new CustomException(SuggestionErrorCode.NOT_FOUND));
    }

    @Override
    @Transactional
    public void updateReviewStatus(Long id, SupportSuggestionUpdateDto updateDto) {
        SupportSuggestionEntity entity = getEntityOrThrow(id);

        entity.setSuggestionReviewStatus(updateDto.getSuggestionReviewStatus());
        entity.setSuggestionReviewer(updateDto.getSuggestionReviewer());
        entity.setSuggestionReviewedAt(updateDto.getSuggestionReviewedAt());

        try {
            suggestionRepository.save(entity);
        } catch (Exception e) {
            throw new CustomException(SuggestionErrorCode.UPDATE_FAILED);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public ResponseEntity<Resource> fileDownload(Long suggestionFileId) {
        // 1. DB에서 파일 정보 조회
        SupportSuggestionFileEntity fileEntity = suggestionFileRepository.findById(suggestionFileId)
                .orElseThrow(() -> new CustomException(SuggestionErrorCode.FILE_NOT_FOUND));

        // 2. 서버 저장 경로 그대로 사용
        String savedFilePath = fileEntity.getSuggestionFilePath();
        File file = new File(savedFilePath);

        if (!file.exists() || !file.isFile()) {
            throw new CustomException(SuggestionErrorCode.FILE_NOT_EXIST_ON_DISK);
        }

        // 3. 다운로드용 파일명 인코딩
        String encodedFileName;
        try {
            encodedFileName = URLEncoder.encode(fileEntity.getSuggestionFileName(), "UTF-8").replaceAll("\\+", "%20");
        } catch (UnsupportedEncodingException e) {
            encodedFileName = fileEntity.getSuggestionFileName(); // fallback
        }

        // 4. 응답 생성
        Resource resource = new FileSystemResource(file);
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + encodedFileName + "\"")
                .body(resource);
    }
}
