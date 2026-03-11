package com.example.focusflow.repository;

import com.example.focusflow.entity.TaskEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<TaskEntity, Long> {

    // Tìm tất cả Task của 1 User
    List<TaskEntity> findByUserId(Long userId);

    // Tìm tất cả Task thuộc về 1 Project
    List<TaskEntity> findByProjectId(Long projectId);
}
