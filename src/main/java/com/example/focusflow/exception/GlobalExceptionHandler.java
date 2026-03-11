package com.example.focusflow.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // Bắt mọi thể loại lỗi Runtime và trả về một câu JSON thân thiện
    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleGlobalException(Exception ex) {
        return new ResponseEntity<>("Độ mixi, lỗi rồi: " + ex.getMessage(), HttpStatus.BAD_REQUEST);
    }
}
