package com.boot.loiteBackend.common.util;

public final class YnUtils {
    private YnUtils() {}

    public static boolean isValidYn(String v) {
        if (TextUtils.isBlank(v)) return false;
        String u = v.trim().toUpperCase();
        return "Y".equals(u) || "N".equals(u);
    }

    /** null/blank면 기본값 반환. 값이 있으면 Y/N으로 강제(normalize). */
    public static String normalizeYnOrDefault(String value, String defaultWhenBlank) {
        if (TextUtils.isBlank(value)) return defaultWhenBlank;
        String v = value.trim().toUpperCase();
        return "Y".equals(v) ? "Y" : "N";
    }
}
