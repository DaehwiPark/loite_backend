package com.boot.loiteBackend.admin.home.eventbanner.spec;

import com.boot.loiteBackend.admin.home.eventbanner.dto.AdminHomeEventBannerListRequestDto;
import com.boot.loiteBackend.common.jpa.SpecUtils;
import com.boot.loiteBackend.common.util.TextUtils;
import com.boot.loiteBackend.domain.home.eventbanner.entity.HomeEventBannerEntity;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 이벤트 배너 목록/노출용 Specification 모음
 */
public final class AdminHomeEventBannerSpecification {

    private AdminHomeEventBannerSpecification() {
    }

    /**
     * 관리자 목록 조회용 스펙
     * - bannerTitle: 부분 일치(대소문자 무시)
     * - displayYn: 정확 일치(대소문자 무시)
     * - startFrom: startAt >= startFrom
     * - endTo:     endAt   <= endTo
     */
    public static Specification<HomeEventBannerEntity> buildListSpec(AdminHomeEventBannerListRequestDto filter) {
        return (root, query, cb) -> {
            List<Predicate> ps = new ArrayList<>();

            if (filter != null) {
                // 제목 LIKE
                if (!TextUtils.isBlank(filter.getBannerTitle())) {
                    Predicate pTitle = SpecUtils.likeLowerContains(cb, root.get("bannerTitle"), filter.getBannerTitle());
                    if (pTitle != null) ps.add(pTitle);
                }

                // 노출 여부(Y/N)
                if (!TextUtils.isBlank(filter.getDisplayYn())) {
                    Predicate pDisplay = SpecUtils.equalsIgnoreCase(cb, root.get("displayYn"), filter.getDisplayYn());
                    if (pDisplay != null) ps.add(pDisplay);
                }

                // 기간
                if (filter.getStartFrom() != null) {
                    ps.add(SpecUtils.gte(cb, root.get("startAt"), filter.getStartFrom()));
                }
                if (filter.getEndTo() != null) {
                    ps.add(SpecUtils.lte(cb, root.get("endAt"), filter.getEndTo()));
                }
            }

            return ps.isEmpty() ? cb.conjunction() : cb.and(ps.toArray(new Predicate[0]));
        };
    }

    /**
     * (옵션) 프론트 노출용 스펙
     * - displayYn = 'Y'
     * - (startAt IS NULL OR startAt <= now)
     * - (endAt   IS NULL OR endAt   >= now)
     */
    public static Specification<HomeEventBannerEntity> buildActiveNowSpec(LocalDateTime now) {
        return (root, query, cb) -> cb.and(
                cb.equal(cb.upper(root.get("displayYn")), "Y"),
                cb.or(cb.isNull(root.get("startAt")), cb.lessThanOrEqualTo(root.get("startAt"), now)),
                cb.or(cb.isNull(root.get("endAt")), cb.greaterThanOrEqualTo(root.get("endAt"), now))
        );
    }
}
