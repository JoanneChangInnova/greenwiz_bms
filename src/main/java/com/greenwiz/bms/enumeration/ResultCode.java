package com.greenwiz.bms.enumeration;

import com.greenwiz.bms.exception.IBmsException;
import lombok.Getter;

@Getter
public enum ResultCode implements IBmsException {

    /**
     * 同一時間有其他人修改資料，請重新再試。
     */
    VERSION_INVALID("同一時間有其他人修改資料，請重新再試。"),

    NAME_DUPLICATE("名稱重複")
    ;

    private final String value;

    ResultCode(String value) {this.value = value;}
}
