package com.greenwiz.bms.utils;

import com.greenwiz.bms.enumeration.ResultCode;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.Map;

public class ExceptionUtils {

    /**
     * 傳入ResultCode，包裝成500錯誤訊息給前端
     * 範例：傳入BM01，返回：{"detail":"同一時間有其他人修改資料，請重新再試。","status":"BM01"}
     */
    public static ResponseEntity<Map<String, String>> serverErrorResp(ResultCode bmsResultCode) {
        Map<String, String> response = new HashMap<>();
        response.put("code", bmsResultCode.name());
        response.put("msg", bmsResultCode.getValue());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
}
