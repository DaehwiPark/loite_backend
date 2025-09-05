package com.boot.loiteBackend.common.util;

import org.springframework.data.domain.*;

public class PageUtils {

    private PageUtils() {}

    public static Pageable safePageable(Pageable pageable, Sort defaultSort) {
        int page = Math.max(0, pageable.getPageNumber());
        int size = Math.max(1, pageable.getPageSize());
        Sort sort = (pageable.getSort() == null || pageable.getSort().isUnsorted())
                ? defaultSort
                : pageable.getSort();
        return PageRequest.of(page, size, sort);
    }

    // 0페이지 고정 PageRequest
    public static Pageable firstPage(int size, Sort sort) {
        int safeSize = Math.max(1, size);
        Sort safeSort = (sort == null || sort.isUnsorted())
                ? Sort.unsorted()
                : sort;
        return PageRequest.of(0, safeSize, safeSort);
    }

}
