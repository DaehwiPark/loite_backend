package com.boot.loiteBackend.web.support.resource.service;

import com.boot.loiteBackend.web.support.resource.dto.SupportResourceDto;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

public interface SupportResourceService {

    Page<SupportResourceDto> getManuals(String keyword, Pageable pageable);

    ResponseEntity<Resource> fileDownload(Long id);

}
