package com.boot.loiteBackend.admin.support.counsel.spec;

import com.boot.loiteBackend.admin.support.counsel.entity.AdminSupportCounselEntity;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

public class AdminSupportCounselSpecification {

    // 키워드로 제목 또는 내용 검색
    public static Specification<AdminSupportCounselEntity> containsKeyword(String keyword) {
        return (root, query, cb) -> {
            if (keyword == null || keyword.isBlank()) return null;

            String likeKeyword = "%" + keyword.toLowerCase() + "%";
            Predicate titlePredicate = cb.like(cb.lower(root.get("counselTitle")), likeKeyword);
            Predicate contentPredicate = cb.like(cb.lower(root.get("counselContent")), likeKeyword);
            return cb.or(titlePredicate, contentPredicate);
        };
    }

    // 삭제 여부 필터
    public static Specification<AdminSupportCounselEntity> isNotDeleted() {
        return (root, query, cb) -> cb.equal(root.get("deleteYn"), "N");
    }

    // 미답변 필터
    public static Specification<AdminSupportCounselEntity> isUnanswered() {
        return (root, query, cb) -> cb.isNull(root.get("counselReplyContent"));
    }
}
