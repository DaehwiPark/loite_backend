package com.boot.loiteBackend.admin.product.product.spec;

import com.boot.loiteBackend.admin.product.product.entity.ProductEntity;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class ProductSpecification {

    // 삭제 여부 필터
    public static Specification<ProductEntity> isNotDeleted() {
        return (root, query, cb) -> cb.equal(root.get("deleteYn"), "N");
    }

    // 키워드로 제목 또는 내용 검색
    public static Specification<ProductEntity> containsKeyword(String keyword) {
        return (root, query, cb) -> {
            if (keyword == null || keyword.isBlank()) return null;

            String pattern = "%" + keyword + "%";
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(cb.like(root.get("productName"), pattern));
            predicates.add(cb.like(root.get("productModelName"), pattern));
            predicates.add(cb.like(root.get("productSummary"), pattern));
            return cb.or(predicates.toArray(new Predicate[0]));
        };
    }
}