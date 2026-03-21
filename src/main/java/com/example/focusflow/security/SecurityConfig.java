package com.example.focusflow.security;

import com.example.focusflow.exception.JwtAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    @Autowired
    private JwtAuthenticationFilter jwtAuthFilter; // Gọi thằng bảo vệ ra

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                // App mình xài JWT (vé cầm tay) nên đéo cần lưu Session (nhớ mặt) nữa
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/auth/**").permitAll() // Đăng ký, Đăng nhập ai cũng vào được
                        .requestMatchers("/error").permitAll()

                        // PHÂN QUYỀN (MỤC: MANAGER tạo project)
                        // Chỉ mấy thằng mang thẻ ROLE_MANAGER mới được vác mặt vào gọi API POST /api/projects
                        .requestMatchers(HttpMethod.POST, "/api/projects/**").hasAuthority("ROLE_MANAGER")

                        // Tất cả các API còn lại đều phải có vé JWT (đã đăng nhập)
                        .anyRequest().authenticated()
                );

        // Lệnh cực kỳ quan trọng: Nhét thằng bảo vệ soi vé lên đứng trước thằng bảo vệ gốc của Spring
        http.addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
