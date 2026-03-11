package com.example.focusflow.controller;

import com.example.focusflow.entity.UserEntity;
import com.example.focusflow.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    // API 1: Lấy danh sách toàn bộ User
    // Chạy khi gọi GET: http://localhost:8080/api/users
    @GetMapping
    public List<UserEntity> getAllUsers() {
        return userService.getAllUsers();
    }

    // API 2: Thêm mới 1 User
    // POST: http://localhost:8080/api/users
    @PostMapping
    public UserEntity createUser(@RequestBody UserEntity newUser) {
        return userService.createUser(newUser);
    }
}