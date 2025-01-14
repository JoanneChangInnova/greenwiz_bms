package com.greenwiz.bms.utils;

import com.greenwiz.bms.entity.BaseEntity;
import com.greenwiz.bms.exception.BmsException;

import java.time.LocalDateTime;
import java.util.Objects;

import static com.greenwiz.bms.enumeration.ResultCode.VERSION_INVALID;
import static com.greenwiz.bms.enumeration.ResultCode.NAME_DUPLICATE;

public class ValidationUtils {

    /**
     * 樂觀鎖檢查
     * @param newDtModify 更新資料取得的時間
     * @param oldDtModify 資料庫裡的時間
     */
    public static void validateVersion(LocalDateTime newDtModify, LocalDateTime oldDtModify) {
        if (!Objects.equals(newDtModify, oldDtModify)) {
            throw new BmsException(VERSION_INVALID);
        }
    }

    public static void checkNameDuplicate(BaseEntity entity, Long reqEntityId, Long entityId) {
        // 檢查新增操作
        if (reqEntityId == null) {
            if (entity != null) {
                // 新增時，若 entity 不為 null，則名稱已經存在，拋出異常
                throw new BmsException(NAME_DUPLICATE);
            }
        }
        // 檢查更新操作
        else {
            if (entity != null && !reqEntityId.equals(entityId)) {
                // 更新時，若 entity 不為 null 且 reqEntityId 與查到的 entityId 不一致，則名稱重複
                throw new BmsException(NAME_DUPLICATE);
            }
        }
    }
}
