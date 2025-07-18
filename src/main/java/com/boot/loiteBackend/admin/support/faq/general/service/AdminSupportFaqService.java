package com.boot.loiteBackend.admin.support.faq.general.service;


import com.boot.loiteBackend.admin.support.faq.general.dto.AdminSupportFaqDto;
import com.boot.loiteBackend.admin.support.faq.general.dto.AdminSupportFaqRequestDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AdminSupportFaqService {

    Page<AdminSupportFaqDto> getPagedFaqs(String keyword, Pageable pageable);

    AdminSupportFaqDto getFaqById(Long id);

    AdminSupportFaqDto updateFaq(Long id, AdminSupportFaqRequestDto request);

    void deleteFaqById(Long id);

    AdminSupportFaqDto createFaq(AdminSupportFaqRequestDto request);
}
