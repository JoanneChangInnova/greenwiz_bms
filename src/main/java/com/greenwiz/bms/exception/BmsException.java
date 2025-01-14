package com.greenwiz.bms.exception;

import com.greenwiz.bms.enumeration.ResultCode;
import io.micrometer.common.util.StringUtils;
import lombok.Getter;

@Getter
public class BmsException extends RuntimeException {

    private final String resultCode;
    private final String resultMsg;


    public BmsException(ResultCode resultCode) {
        this(resultCode.name(), resultCode.getValue(), null);
    }

    public BmsException(String resultCode, String msg, Throwable t) {
        super(StringUtils.isEmpty(msg) ? resultCode : msg, t);
        this.resultCode = resultCode;
        this.resultMsg = msg;
    }

}
