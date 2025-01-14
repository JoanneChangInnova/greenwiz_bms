package com.greenwiz.bms.utils;

import com.greenwiz.bms.exception.BmsException;

import java.time.LocalDateTime;
import java.util.Objects;

import static com.greenwiz.bms.enumeration.ResultCode.VERSION_INVALID;

public class ValidationUtils {

    /**
     * 樂觀鎖檢查
     *
     * @param newDtModify 更新資料取得的時間
     * @param oldDtModify 資料庫裡的時間
     */
    public static void validateVersion(LocalDateTime newDtModify, LocalDateTime oldDtModify) {
        if (!Objects.equals(newDtModify, oldDtModify)) {
            throw new BmsException(VERSION_INVALID);
        }
    }

}
