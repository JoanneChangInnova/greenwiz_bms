package com.greenwiz.bms.exception;

import com.greenwiz.bms.enumeration.ResultCode;

public interface IBmsException {

    default BmsException SvcException(ResultCode resultCode) {
        return new BmsException(resultCode);
    }


}
