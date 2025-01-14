package com.greenwiz.bms.utils;

import java.time.LocalDateTime;
import java.util.Objects;

public class ValidationUtils {

    /**
     * 樂觀鎖檢查
     * @param newDtModify 更新資料取得的時間
     * @param oldDtModify 資料庫裡的時間
     */
    public static void validateVersion(LocalDateTime newDtModify, LocalDateTime oldDtModify) {
        if (!Objects.equals(newDtModify, oldDtModify)) {
            throw new RuntimeException("BASE_VERSION_INVALID");
        }
    }
}
