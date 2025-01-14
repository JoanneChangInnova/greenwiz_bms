package com.greenwiz.bms.exception;

import com.greenwiz.bms.enumeration.ResultCode;

public interface IBmsException {

    default BmsException Bmsxception(ResultCode resultCode) {
        return new BmsException(resultCode);
    }


}
