package com.example.community.common.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(BaseException.class)
    public ResponseEntity<Map<String, Object>> handleCustomException(BaseException ex) {
        ErrorCode code = ex.getErrorCode();

        ex.printStackTrace();

        Map<String, Object> body = new HashMap<>();
        body.put("message", code.getMessage());
        body.put("data", ex.getMessage());

        return ResponseEntity
                .status(code.getStatus())
                .body(body);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleException(Exception ex) {

        ex.printStackTrace();

        Map<String, Object> body = new HashMap<>();
        body.put("message", "internal_server_error");
        body.put("data", ex.getMessage());

        return ResponseEntity
                .status(500)
                .body(body);
    }
}
