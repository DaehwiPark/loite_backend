package com.boot.loiteBackend.common.jpa;

import com.boot.loiteBackend.common.util.TextUtils;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;


public final class SpecUtils {
    private SpecUtils() {}

    /** lower(field) like %kw% */
    public static Predicate likeLowerContains(CriteriaBuilder cb, Expression<String> path, String keyword) {
        if (TextUtils.isBlank(keyword)) return null;
        String pattern = "%" + keyword.trim().toLowerCase() + "%";
        return cb.like(cb.lower(path), pattern);
    }

    /** field >= from (inclusive) */
    public static <T extends Comparable<? super T>> Predicate gte(CriteriaBuilder cb, Expression<T> path, T from) {
        return (from == null) ? null : cb.greaterThanOrEqualTo(path, from);
    }

    /** field <= to (inclusive) */
    public static <T extends Comparable<? super T>> Predicate lte(CriteriaBuilder cb, Expression<T> path, T to) {
        return (to == null) ? null : cb.lessThanOrEqualTo(path, to);
    }

    /** upper(field) = upper(value) */
    public static Predicate equalsIgnoreCase(CriteriaBuilder cb, Expression<String> path, String value) {
        if (TextUtils.isBlank(value)) return null;
        return cb.equal(cb.upper(path), value.trim().toUpperCase());
    }

    /** deletedYn = 'N' */
    public static Predicate notDeleted(CriteriaBuilder cb, Root<?> root) {
        return cb.equal(cb.upper(root.get("deletedYn")), "N");
    }
}
