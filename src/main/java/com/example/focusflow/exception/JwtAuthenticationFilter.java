package com.example.focusflow.exception;

import com.example.focusflow.security.JwtUtil;
import com.example.focusflow.service.CustomUserDetailsService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        // 1. Lấy cái Header "Authorization" từ Postman gửi lên
        final String authHeader = request.getHeader("Authorization");
        String username = null;
        String jwtToken = null;

        // 2. Kiểm tra xem có vé không và vé có bắt đầu bằng chữ "Bearer " không
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            jwtToken = authHeader.substring(7); // Cắt 7 chữ cái đầu ("Bearer ") đi để lấy lõi Token
            try {
                username = jwtUtil.extractUsername(jwtToken); // Nhờ JwtUtil giải mã lấy tên
            } catch (Exception e) {
                System.out.println("Vé giả, hết hạn hoặc bị sửa đổi cmnr!");
            }
        }

        // 3. Nếu vé chuẩn và thằng này chưa được cấp quyền trong lượt request này
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = customUserDetailsService.loadUserByUsername(username);

            // Soi xem vé có khớp với user không, còn hạn không
            if (jwtUtil.isTokenValid(jwtToken, userDetails.getUsername())) {
                // Hợp lệ -> Cho phép đi qua cửa
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                // Nhét thẻ VIP vào tay nó để nó tung tăng trong app
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }

        // Bấm nút cho đi tiếp đến Controller
        filterChain.doFilter(request, response);
    }
}
