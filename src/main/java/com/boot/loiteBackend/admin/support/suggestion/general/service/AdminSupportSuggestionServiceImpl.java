package com.boot.loiteBackend.admin.support.suggestion.general.service;

import com.boot.loiteBackend.global.error.exception.CustomException;
import com.boot.loiteBackend.admin.support.suggestion.file.dto.AdminSupportSuggestionFileSummaryDto;
import com.boot.loiteBackend.admin.support.suggestion.file.entity.AdminSupportSuggestionFileEntity;
import com.boot.loiteBackend.admin.support.suggestion.file.repository.AdminSupportSuggestionFileRepository;
import com.boot.loiteBackend.admin.support.suggestion.general.dto.AdminSupportSuggestionDto;
import com.boot.loiteBackend.admin.support.suggestion.general.dto.AdminSupportSuggestionUpdateDto;
import com.boot.loiteBackend.admin.support.suggestion.general.entity.AdminSupportSuggestionEntity;
import com.boot.loiteBackend.admin.support.suggestion.general.error.AdminSuggestionErrorCode;
import com.boot.loiteBackend.admin.support.suggestion.general.mapper.AdminSupportSuggestionMapper;
import com.boot.loiteBackend.admin.support.suggestion.general.repository.AdminSupportSuggestionRepository;
import com.boot.loiteBackend.admin.user.dto.AdminUserSummaryDto;
import com.boot.loiteBackend.admin.user.repository.AdminUserRepository;
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
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminSupportSuggestionServiceImpl implements AdminSupportSuggestionService {

    private final AdminSupportSuggestionRepository suggestionRepository;
    private final AdminSupportSuggestionMapper adminSupportSuggestionMapper;
    private final AdminSupportSuggestionFileRepository suggestionFileRepository;
    private final AdminUserRepository adminUserRepository;

    @Override
    @Transactional(readOnly = true)
    public Page<AdminSupportSuggestionDto> getPagedSuggestions(String keyword, Pageable pageable) {
        Page<AdminSupportSuggestionEntity> page;

        if (StringUtils.hasText(keyword)) {
            page = suggestionRepository.findByKeywordAndDeleteYn(keyword, "N", pageable);
        } else {
            page = suggestionRepository.findByDeleteYn("N", pageable);
        }

        return page.map(entity -> {
            AdminSupportSuggestionDto dto = adminSupportSuggestionMapper.toDto(entity);

            // 사용자 정보 주입
            if (entity.getSuggestionUserId() != null) {
                AdminUserSummaryDto userDto = adminUserRepository.findById(entity.getSuggestionUserId())
                        .map(user -> AdminUserSummaryDto.builder()
                                .userId(user.getUserId())
                                .name(user.getName())
                                .build())
                        .orElse(null);
                dto.setAdminUserSummaryDto(userDto);
            }

            return dto;
        });
    }


    @Override
    @Transactional(readOnly = true)
    public AdminSupportSuggestionDto getSuggestionById(Long id) {
        AdminSupportSuggestionEntity entity = getEntityOrThrow(id);
        AdminSupportSuggestionDto dto = adminSupportSuggestionMapper.toDto(entity);

        // 첨부파일 정보 주입
        List<AdminSupportSuggestionFileSummaryDto> fileDtos = suggestionFileRepository.findBySuggestionId(id)
                .stream()
                .map(file -> AdminSupportSuggestionFileSummaryDto.builder()
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
            AdminUserSummaryDto userDto = adminUserRepository.findById(entity.getSuggestionUserId())
                    .map(user -> AdminUserSummaryDto.builder()
                            .userId(user.getUserId())
                            .userEmail(user.getUserEmail())
                            .name(user.getName())
                            .role(user.getRole())
                            .status(user.getStatus())
                            .createdAt(user.getCreatedAt())
                            .build())
                    .orElse(null);
            dto.setAdminUserSummaryDto(userDto);
        }

        return dto;
    }

    @Override
    @Transactional
    public void deleteSuggestion(Long id) {
        AdminSupportSuggestionEntity entity = getEntityOrThrow(id);
        try {
            entity.setDeleteYn("Y");
            suggestionRepository.save(entity);
        } catch (Exception e) {
            throw new CustomException(AdminSuggestionErrorCode.DELETE_FAILED);
        }
    }

    private AdminSupportSuggestionEntity getEntityOrThrow(Long id) {
        return suggestionRepository.findBySuggestionIdAndDeleteYn(id, "N")
                .orElseThrow(() -> new CustomException(AdminSuggestionErrorCode.NOT_FOUND));
    }

    @Override
    @Transactional
    public void updateReviewStatus(Long id, AdminSupportSuggestionUpdateDto updateDto) {
        AdminSupportSuggestionEntity entity = getEntityOrThrow(id);

        entity.setSuggestionReviewStatus(updateDto.getSuggestionReviewStatus());
        entity.setSuggestionReviewer(updateDto.getSuggestionReviewer());
        entity.setSuggestionReviewedAt(updateDto.getSuggestionReviewedAt());

        try {
            suggestionRepository.save(entity);
        } catch (Exception e) {
            throw new CustomException(AdminSuggestionErrorCode.UPDATE_FAILED);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public ResponseEntity<Resource> fileDownload(Long suggestionFileId) {
        // 1. DB에서 파일 정보 조회
        AdminSupportSuggestionFileEntity fileEntity = suggestionFileRepository.findById(suggestionFileId)
                .orElseThrow(() -> new CustomException(AdminSuggestionErrorCode.FILE_NOT_FOUND));

        // 2. 서버 저장 경로 그대로 사용
        String savedFilePath = fileEntity.getSuggestionFilePath();
        File file = new File(savedFilePath);

        if (!file.exists() || !file.isFile()) {
            throw new CustomException(AdminSuggestionErrorCode.FILE_NOT_EXIST_ON_DISK);
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
