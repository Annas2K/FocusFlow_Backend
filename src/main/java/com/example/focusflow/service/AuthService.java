package com.example.focusflow.service;

import com.example.focusflow.dto.AuthResponse;
import com.example.focusflow.dto.LoginRequest;
import com.example.focusflow.dto.RegisterRequest;
import com.example.focusflow.entity.RoleEntity;
import com.example.focusflow.entity.UserEntity;
import com.example.focusflow.exception.AppException;
import com.example.focusflow.repository.RoleRepository;
import com.example.focusflow.repository.UserRepository;
import com.example.focusflow.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder; // Gọi máy băm ra

    @Autowired
    private JwtUtil jwtUtil; // Gọi máy in vé ra (MỤC TẠO JWT)

    // ================== ĐĂNG KÝ ==================
    public UserEntity register(RegisterRequest request) {
        // 1. Check trùng lặp
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new AppException(400, "Tên đăng nhập đã tồn tại!");
        }
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new AppException(400, "Email đã được sử dụng!");
        }

        // 2. Tạo User mới
        UserEntity user = new UserEntity();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());

        // MỤC 4: Băm password trước khi lưu
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        // 3. Cấp quyền mặc định là ROLE_USER
        RoleEntity userRole = roleRepository.findByName("ROLE_MANAGER")
                .orElseThrow(() -> new AppException(500, "Lỗi hệ thống: Role đâu?!"));
        Set<RoleEntity> roles = new HashSet<>();
        roles.add(userRole);
        user.setRoles(roles);

        // 4. Lưu xuống DB
        return userRepository.save(user);
    }

    // ================== ĐĂNG NHẬP ==================
    public AuthResponse login(LoginRequest request) {
        // 1. Tìm thằng User trong Database (Không có thì chửi 400)
        UserEntity user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new AppException(400, "Sai tên đăng nhập hoặc mật khẩu!"));

        // 2. Check Password (so sánh pass nhập vào với pass đã băm trong DB)
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new AppException(400, "Sai tên đăng nhập hoặc mật khẩu!");
        }

        // 3. In vé (Tạo Token JWT)
        String token = jwtUtil.generateToken(user.getUsername());

        // 4. Trả vé về cho Client
        return AuthResponse.builder()
                .token(token)
                .type("Bearer")
                .build();
    }
}