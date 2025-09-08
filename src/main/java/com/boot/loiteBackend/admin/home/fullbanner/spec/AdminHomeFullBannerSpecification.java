package com.boot.loiteBackend.admin.home.fullbanner.spec;

import com.boot.loiteBackend.admin.home.fullbanner.dto.AdminHomeFullBannerListRequestDto;
import com.boot.loiteBackend.common.jpa.SpecUtils;
import com.boot.loiteBackend.common.util.TextUtils;
import com.boot.loiteBackend.domain.home.fullbanner.entity.HomeFullBannerEntity;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public final class AdminHomeFullBannerSpecification {
    private AdminHomeFullBannerSpecification() {
    }

    public static Specification<HomeFullBannerEntity> buildListSpec(AdminHomeFullBannerListRequestDto filter) {
        return (root, query, cb) -> {
            List<Predicate> ps = new ArrayList<>();

            // displayYn
            if (filter != null) {
                if (!TextUtils.isBlank(filter.getDisplayYn())) {
                    ps.add(cb.equal(cb.upper(root.get("displayYn")),
                            filter.getDisplayYn().trim().toUpperCase()));
                }
                if (filter.getStartFrom() != null) {
                    ps.add(SpecUtils.gte(cb, root.get("startAt"), filter.getStartFrom()));
                }
                if (filter.getEndTo() != null) {
                    ps.add(SpecUtils.lte(cb, root.get("endAt"), filter.getEndTo()));
                }
                if (!TextUtils.isBlank(filter.getKeyword())) {
                    String kw = filter.getKeyword();
                    List<Predicate> ors = new ArrayList<>();
                    ors.add(SpecUtils.likeLowerContains(cb, root.get("title"), kw));
                    ors.add(SpecUtils.likeLowerContains(cb, root.get("subtitle"), kw));
                    ors.add(SpecUtils.likeLowerContains(cb, root.get("buttonText"), kw));
                    ors.add(SpecUtils.likeLowerContains(cb, root.get("buttonLinkUrl"), kw));
                    ors.removeIf(p -> p == null);
                    if (!ors.isEmpty()) {
                        ps.add(cb.or(ors.toArray(new Predicate[0])));
                    }
                }
            }

            return ps.isEmpty() ? cb.conjunction() : cb.and(ps.toArray(new Predicate[0]));
        };
    }
}
