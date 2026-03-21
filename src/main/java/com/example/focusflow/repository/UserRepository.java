package com.example.focusflow.repository;

import com.example.focusflow.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
    // Thêm dòng này vào nhé!
    java.util.Optional<com.example.focusflow.entity.UserEntity> findByUsername(String username);
}
