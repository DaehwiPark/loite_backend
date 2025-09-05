package com.boot.loiteBackend.common.util;

public final class TextUtils {
    private TextUtils() {}

    public static boolean isBlank(String s) {
        return s == null || s.trim().isEmpty();
    }

    public static String trimToNull(String s) {
        if (isBlank(s)) return null;
        String v = s.trim();
        return v.isEmpty() ? null : v;
    }
}
