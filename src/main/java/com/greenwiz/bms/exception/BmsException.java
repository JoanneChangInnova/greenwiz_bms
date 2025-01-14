package com.greenwiz.bms.exception;

import com.greenwiz.bms.enumeration.ResultCode;
import io.micrometer.common.util.StringUtils;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class BmsException extends RuntimeException {

    private final String resultCode;
    private final String resultMsg;


    /**
     * 前端可依ResultCode自定義的代碼，進行特殊處理
     * 例如ResultCode = VERSION_INVALID時，需跳轉回前頁
     */
    public BmsException(ResultCode resultCode) {
        this(resultCode.name(), resultCode.getValue(), null);
    }

    /**
     * 使用者Request有誤時，使用此方法傳入錯誤訊息給前端
     */
    public BmsException(String resultMsg) {
        this(HttpStatus.BAD_REQUEST.name(), resultMsg, null);
    }

    public BmsException(String resultCode, String resultMsg, Throwable t) {
        super(StringUtils.isEmpty(resultMsg) ? resultCode : resultMsg, t);
        this.resultCode = resultCode;
        this.resultMsg = resultMsg;
    }

}
