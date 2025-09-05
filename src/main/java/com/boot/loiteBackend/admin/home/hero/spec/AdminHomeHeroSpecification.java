package com.boot.loiteBackend.admin.home.hero.spec;

import com.boot.loiteBackend.admin.home.hero.dto.AdminHomeHeroListRequestDto;
import com.boot.loiteBackend.common.jpa.SpecUtils;
import com.boot.loiteBackend.common.util.TextUtils;
import com.boot.loiteBackend.domain.home.hero.entity.HomeHeroEntity;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

/**
 * Hero 목록 검색용 Specification 모음
 */
public final class AdminHomeHeroSpecification {

    private AdminHomeHeroSpecification() {
    }

    /**
     * 목록 검색용 스펙 (엔티티 필드: titleText, bodyText, buttonText, buttonLink 등)
     */
    public static Specification<HomeHeroEntity> buildListSpec(AdminHomeHeroListRequestDto filter) {
        return (root, query, cb) -> {
            List<Predicate> ands = new ArrayList<>();

            // 1) 소프트 삭제 제외 (deletedYn = 'N')
            Predicate pNotDeleted = SpecUtils.notDeleted(cb, root); // 공통 유틸 사용
            if (pNotDeleted != null) ands.add(pNotDeleted);

            if (filter != null) {
                // 2) displayYn(Y/N)
                Predicate pDisplay = SpecUtils.equalsIgnoreCase(cb, root.get("displayYn"), filter.getDisplayYn());
                if (pDisplay != null) ands.add(pDisplay);

                // 3) 기간 필터 (inclusive)
                Predicate pFrom = SpecUtils.gte(cb, root.get("startAt"), filter.getStartFrom());
                if (pFrom != null) ands.add(pFrom);

                Predicate pTo = SpecUtils.lte(cb, root.get("endAt"), filter.getEndTo());
                if (pTo != null) ands.add(pTo);

                // 4) 키워드: titleText / bodyText / buttonText / buttonLink 부분일치 (OR)
                if (!TextUtils.isBlank(filter.getKeyword())) {
                    String kw = filter.getKeyword();
                    List<Predicate> ors = new ArrayList<>();
                    Predicate p1 = SpecUtils.likeLowerContains(cb, root.get("titleText"), kw);
                    Predicate p2 = SpecUtils.likeLowerContains(cb, root.get("bodyText"), kw);
                    Predicate p3 = SpecUtils.likeLowerContains(cb, root.get("buttonText"), kw);
                    Predicate p4 = SpecUtils.likeLowerContains(cb, root.get("buttonLink"), kw);

                    if (p1 != null) ors.add(p1);
                    if (p2 != null) ors.add(p2);
                    if (p3 != null) ors.add(p3);
                    if (p4 != null) ors.add(p4);

                    if (!ors.isEmpty()) {
                        ands.add(cb.or(ors.toArray(new Predicate[0])));
                    }
                }
            }

            return ands.isEmpty() ? cb.conjunction() : cb.and(ands.toArray(new Predicate[0]));
        };
    }
}
