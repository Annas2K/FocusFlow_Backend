package com.example.focusflow.controller;

import com.example.focusflow.entity.TaskEntity;
import com.example.focusflow.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    @Autowired
    private TaskService taskService;

    // API lấy toàn bộ task
    @GetMapping
    public List<TaskEntity> getAllTasks() {
        return taskService.getAllTasks();
    }

    // API thêm task mới
    @PostMapping
    public TaskEntity createTask(@RequestBody TaskEntity newTask) {
        return taskService.createTask(newTask);
    }

    // API list task theo user
    // Chạy khi gọi GET: http://localhost:8080/api/tasks/user/3
    @GetMapping("/user/{userId}")
    public List<TaskEntity> getTasksByUserId(@PathVariable Long userId) {
        return taskService.getTasksByUserId(userId);
    }

    // API list task theo project
    // Chạy khi gọi GET: http://localhost:8080/api/tasks/project/1
    @GetMapping("/project/{projectId}")
    public List<TaskEntity> getTasksByProjectId(@PathVariable Long projectId) {
        return taskService.getTasksByProjectId(projectId);
    }
}
