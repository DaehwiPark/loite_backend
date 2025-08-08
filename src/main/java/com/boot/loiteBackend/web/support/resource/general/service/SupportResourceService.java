package com.boot.loiteBackend.web.support.resource.general.service;

import com.boot.loiteBackend.web.support.resource.general.dto.SupportResourceDto;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

public interface SupportResourceService {

    Page<SupportResourceDto> getManuals(String keyword, Long categoryId, Pageable pageable);

    ResponseEntity<Resource> fileDownload(Long resourceId);

}
