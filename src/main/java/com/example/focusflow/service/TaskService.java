package com.example.focusflow.service;

import com.example.focusflow.entity.TaskEntity;
import com.example.focusflow.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskService {

    @Autowired
    private TaskRepository taskRepository;

    // Lấy tất cả Task
    public List<TaskEntity> getAllTasks() {
        return taskRepository.findAll();
    }

    // Lấy Task theo User ID
    public List<TaskEntity> getTasksByUserId(Long userId) {
        return taskRepository.findByUserId(userId);
    }

    // Lấy Task theo Project ID
    public List<TaskEntity> getTasksByProjectId(Long projectId) {
        return taskRepository.findByProjectId(projectId);
    }

    // Thêm Task mới
    public TaskEntity createTask(TaskEntity newTask) {
        return taskRepository.save(newTask);
    }
}
