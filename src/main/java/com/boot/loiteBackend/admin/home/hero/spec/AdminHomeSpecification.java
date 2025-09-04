package com.boot.loiteBackend.admin.home.hero.spec;

import com.boot.loiteBackend.admin.home.hero.dto.AdminHomeHeroListRequestDto;
import com.boot.loiteBackend.domain.home.hero.entity.HomeHeroEntity;
import org.springframework.data.jpa.domain.Specification;

import jakarta.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

public class AdminHomeSpecification {

    /**
     * HomeHeroEntity 검색 조건 생성
     * @param filter 목록 요청 DTO
     * @return Specification<HomeHeroEntity>
     */
    public static Specification<HomeHeroEntity> filter(AdminHomeHeroListRequestDto filter) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            // 소프트 삭제 제외
            predicates.add(cb.equal(cb.upper(root.get("deletedYn")), "N"));

            if (filter != null) {
                // 발행 상태 필터
                if (filter.getPublishStatus() != null && !filter.getPublishStatus().isBlank()) {
                    predicates.add(cb.equal(root.get("publishStatus"), filter.getPublishStatus()));
                }
                // 노출 여부 필터
                if (filter.getDisplayYn() != null && !filter.getDisplayYn().isBlank()) {
                    predicates.add(cb.equal(cb.upper(root.get("displayYn")), filter.getDisplayYn().toUpperCase()));
                }
                // 시작일 필터
                if (filter.getStartFrom() != null) {
                    predicates.add(cb.greaterThanOrEqualTo(root.get("startAt"), filter.getStartFrom()));
                }
                // 종료일 필터
                if (filter.getEndTo() != null) {
                    predicates.add(cb.lessThanOrEqualTo(root.get("endAt"), filter.getEndTo()));
                }
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
