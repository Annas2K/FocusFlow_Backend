package com.example.focusflow.service;

import com.example.focusflow.entity.UserEntity;
import com.example.focusflow.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    // Hàm lấy toàn bộ danh sách User từ DB
    public List<UserEntity> getAllUsers() {
        return userRepository.findAll();
    }

    // Hàm tạo User mới
    public UserEntity createUser(UserEntity newUser) {
        // Có thể check logic ở đây (ví dụ: check trùng username) trước khi lưu
        return userRepository.save(newUser);
    }
}
