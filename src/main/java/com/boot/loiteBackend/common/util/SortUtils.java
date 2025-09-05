package com.boot.loiteBackend.common.util;

import org.springframework.data.domain.Sort;

import java.util.Set;

public final class SortUtils {
    private SortUtils() {}

    /**
     * 허용된 컬럼만 남긴 Sort를 반환.
     * - sort가 null/unsorted이거나, 허용 컬럼이 하나도 없으면 fallback 반환
     */
    public static Sort whitelist(Sort sort, Set<String> allowed, Sort fallback) {
        if (allowed == null || allowed.isEmpty()) return (fallback == null ? Sort.unsorted() : fallback);
        if (sort == null || sort.isUnsorted()) return (fallback == null ? Sort.unsorted() : fallback);

        Sort result = Sort.unsorted();
        for (Sort.Order o : sort) {
            if (allowed.contains(o.getProperty())) {
                result = result.and(Sort.by(o));
            }
        }
        return result.isUnsorted() ? (fallback == null ? Sort.unsorted() : fallback) : result;
    }
}
