package com.greenwiz.bms.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice()
public class GlobalExceptionHandler {

    @ExceptionHandler(BmsException.class)
    public ResponseEntity<Map<String, String>> handleSvcException(BmsException e) {
        Map<String, String> response = new HashMap<>();
        response.put("code", e.getResultCode());
        response.put("msg", e.getResultMsg());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }


}
