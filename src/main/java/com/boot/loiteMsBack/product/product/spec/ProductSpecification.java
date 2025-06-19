package com.boot.loiteMsBack.product.product.spec;

import com.boot.loiteMsBack.product.product.entity.ProductEntity;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class ProductSpecification {

    // 삭제 안 된 상품
    public static Specification<ProductEntity> isNotDeleted() {
        return (root, query, cb) -> cb.equal(root.get("deleteYn"), "N");
    }

    // 키워드 검색: 상품명, 모델명, 요약 설명 중 하나라도 포함
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