package com.example.focusflow.controller;

import com.example.focusflow.entity.TaskEntity;
import com.example.focusflow.enums.TaskStatus;
import com.example.focusflow.exception.ApiResponse;
import com.example.focusflow.service.TaskService;
import jakarta.validation.Valid;
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
    public ApiResponse<List<TaskEntity>> getAllTasks() {
        return ApiResponse.<List<TaskEntity>>builder()
                .code(200)
                .message("Lấy danh sách toàn bộ Task thành công!")
                .result(taskService.getAllTasks())
                .build();
    }

    // API thêm task mới
    @PostMapping
    public ApiResponse<TaskEntity> createTask(@Valid @RequestBody TaskEntity newTask) {
        return ApiResponse.<TaskEntity>builder()
                .code(200)
                .message("Tạo Task thành công rồi nhé!")
                .result(taskService.createTask(newTask))
                .build();
    }

    // API list task theo user
    // Chạy khi gọi GET: http://localhost:8080/api/tasks/user/3
    @GetMapping("/user/{userId}")
    public ApiResponse<List<TaskEntity>> getTasksByUserId(@PathVariable Long userId) {
        return ApiResponse.<List<TaskEntity>>builder()
                .code(200)
                .message("Lấy danh sách Task của User thành công!")
                .result(taskService.getTasksByUserId(userId))
                .build();
    }

    // API list task theo project
    // Chạy khi gọi GET: http://localhost:8080/api/tasks/project/1
    @GetMapping("/project/{projectId}")
    public ApiResponse<List<TaskEntity>> getTasksByProjectId(@PathVariable Long projectId) {
        return ApiResponse.<List<TaskEntity>>builder()
                .code(200)
                .message("Lấy danh sách Task của Project thành công!")
                .result(taskService.getTasksByProjectId(projectId))
                .build();
    }

    // Chạy khi gọi PUT: http://localhost:8080/api/tasks/1/assign/2
    @PutMapping("/{taskId}/assign/{userId}")
    public ApiResponse<TaskEntity> assignTask(@PathVariable Long taskId, @PathVariable Long userId) {
        return ApiResponse.<TaskEntity>builder()
                .code(200)
                .message("Giao việc thành công!")
                .result(taskService.assignTask(taskId, userId))
                .build();
    }

    // Chạy khi gọi PATCH: http://localhost:8080/api/tasks/1/status?newStatus=IN_PROGRESS
    @PatchMapping("/{taskId}/status")
    public ApiResponse<TaskEntity> updateTaskStatus(
            @PathVariable Long taskId,
            @RequestParam TaskStatus newStatus) {

        return ApiResponse.<TaskEntity>builder()
                .code(200)
                .message("Cập nhật trạng thái Task thành công!")
                .result(taskService.updateTaskStatus(taskId, newStatus))
                .build();
    }
}
