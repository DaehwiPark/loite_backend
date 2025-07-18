package com.boot.loiteBackend.admin.news.general.service;

import com.boot.loiteBackend.admin.news.general.dto.AdminNewsDto;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface AdminNewsService {

    AdminNewsDto createNews(AdminNewsDto dto, MultipartFile thumbnail);
    AdminNewsDto updateNews(Long id, AdminNewsDto dto);
    void deleteNews(Long id);
    List<AdminNewsDto> getAllNews();
    AdminNewsDto getNewsById(Long id);
}
