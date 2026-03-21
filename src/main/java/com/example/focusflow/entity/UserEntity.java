package com.example.focusflow.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // MSSQL dùng IDENTITY
    private Long id;

    @Column(nullable = false, unique = true, length = 50)
    private String username;

    @Column(nullable = false, unique = true, length = 100)
    private String email;

    // Bắt buộc phải có Password để làm Security Tuần 7 nhé chiến thần!
    @Column(nullable = false)
    private String password;

    // ĐÃ XÓA: private String role;

    // ĐÃ THÊM: Bảng trung gian user_roles
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<RoleEntity> roles;

    // ⚠️ LƯU Ý: Nếu ở dưới mày có đoạn code nối bảng @OneToMany với Task hay @ManyToMany với Project của mấy tuần trước thì mày cứ dán nối tiếp vào đây nhé, đừng có xóa của tao!
}
