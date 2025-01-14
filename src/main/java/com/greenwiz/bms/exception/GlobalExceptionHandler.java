package com.greenwiz.bms.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@ControllerAdvice()
public class GlobalExceptionHandler {

    @ExceptionHandler(BmsException.class)
    public ResponseEntity<Map<String, String>> handleSvcException(BmsException e) {
        Map<String, String> response = new HashMap<>();
        response.put("code", e.getResultCode());
        response.put("msg", e.getResultMsg());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleValidationException(MethodArgumentNotValidException ex) {
        // 獲取所有錯誤訊息
        List<String> errorMessages = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(fieldError -> fieldError.getDefaultMessage())
                .collect(Collectors.toList());

        // 返回400 Bad Request和錯誤訊息
        return ResponseEntity.badRequest().body(errorMessages);
    }


}
