package com.example.focusflow.exception;

import lombok.Getter;

@Getter
public class AppException extends RuntimeException {
    private int code; // Mã lỗi tự chế (VD: 400, 404, 1001...)

    public AppException(int code, String message) {
        super(message);
        this.code = code;
    }
}
