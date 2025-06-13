package com.boot.loiteMsBack.news.general.service;

import com.boot.loiteMsBack.news.general.dto.NewsDto;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface NewsService {

    NewsDto createNews(NewsDto dto, MultipartFile thumbnail);
    NewsDto updateNews(Long id, NewsDto dto);
    void deleteNews(Long id);
    List<NewsDto> getAllNews();
    NewsDto getNewsById(Long id);
}
