package com.example.focusflow.controller;

import com.example.focusflow.dto.AuthResponse;
import com.example.focusflow.dto.LoginRequest;
import com.example.focusflow.dto.RegisterRequest;
import com.example.focusflow.exception.ApiResponse;
import com.example.focusflow.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/register")
    public ApiResponse<String> register(@Valid @RequestBody RegisterRequest request) {
        authService.register(request);
        return ApiResponse.<String>builder()
                .code(200)
                .message("Đăng ký tài khoản thành công!")
                .result("OK")
                .build();
    }
    @PostMapping("/login")
    public ApiResponse<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        return ApiResponse.<AuthResponse>builder()
                .code(200)
                .message("Đăng nhập thành công! Cầm vé vào đi quẩy đi!")
                .result(authService.login(request))
                .build();
    }
}
