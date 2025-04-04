package com.greenwiz.bms.utils;

import com.greenwiz.bms.enumeration.UserRole;
import lombok.Data;

public class ThreadLocalUtils {
    /**
     * 系統使用者(執行排程、系統自動執行)
     */
    public static final String SYSTEM_USER = "SYSTEM";

    private static final ThreadLocal<User> USER = new ThreadLocal<>();

    public static void setUser(User id) {
        USER.set(id);
    }

    public static User getUser() {
        return USER.get();
    }

    public static void cleanAll() {
        USER.remove();
    }

    @Data
    public static class User {
        private UserRole role;
        private Long id;
    }
}
