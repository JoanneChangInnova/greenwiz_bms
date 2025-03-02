package com.greenwiz.bms.utils;

import com.greenwiz.bms.enumeration.DeviceModel;
import com.greenwiz.bms.enumeration.DeviceType;
import com.greenwiz.bms.enumeration.FunctionType;
import com.greenwiz.bms.exception.BmsException;

import java.time.LocalDateTime;
import java.util.Arrays;
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

    /** 驗證通道代號格式 */
    public static void validateChannelName(String channelName) {
        if (!channelName.matches("^[A-Z](-(M|\\d{2}))?(-\\d{2}){0,2}$")) {
            throw new BmsException("通道代號格式不正確，請使用 A-99-99-99 或 A-M");
        }
    }

    /** 驗證設備型號是否合法 */
    public static void validateDeviceModel(DeviceType deviceType, String deviceModel) {
        boolean isValid = switch (deviceType) {
            case MONITOR -> Arrays.stream(DeviceModel.Monitor.values())
                    .anyMatch(m -> m.getDeviceModel().equals(deviceModel));
            case CONTROLLER -> Arrays.stream(DeviceModel.Controller.values())
                    .anyMatch(c -> c.getDeviceModel().equals(deviceModel));
        };

        if (!isValid) {
            throw new BmsException("設備型號 [" + deviceModel + "] 不屬於設備類型 [" + deviceType + "]");
        }
    }

    /** 驗證功能模式是否合法 */
    public static void validateFunctionType(String deviceModel, String functionType) {
        boolean isValid = Arrays.stream(FunctionType.Monitor.values()).anyMatch(f -> f.getFunctionType().equals(functionType)) ||
                Arrays.stream(FunctionType.SWB.values()).anyMatch(f -> f.getFunctionType().equals(functionType)) ||
                Arrays.stream(FunctionType.AmaFans.values()).anyMatch(f -> f.getFunctionType().equals(functionType)) ||
                Arrays.stream(FunctionType.CXIR0001S.values()).anyMatch(f -> f.getFunctionType().equals(functionType));

        if (!isValid) {
            throw new BmsException("功能模式 [" + functionType + "] 不符合設備型號 [" + deviceModel + "]");
        }
    }

}
