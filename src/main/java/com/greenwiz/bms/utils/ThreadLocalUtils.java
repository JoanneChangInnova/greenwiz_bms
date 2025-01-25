package com.greenwiz.bms.utils;

public class ThreadLocalUtils {
    /**
     * 系統使用者(執行排程、系統自動執行)
     */
    public static final String SYSTEM_USER = "SYSTEM";

    private static final ThreadLocal<String> USER = new ThreadLocal<>();
    public static void setUser(String email) {
        USER.set(email);
    }

    public static String getUser() {
        return USER.get();
    }

    public static void cleanAll() {
        USER.remove();
    }
}
