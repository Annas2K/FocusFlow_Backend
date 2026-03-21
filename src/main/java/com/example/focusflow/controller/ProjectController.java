package com.example.focusflow.controller;

import com.example.focusflow.entity.ProjectEntity;
import com.example.focusflow.exception.ApiResponse;
import com.example.focusflow.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/projects")
public class ProjectController {

    @Autowired
    private ProjectService projectService;

    // RULE 1: Chỉ thằng nào mang thẻ MANAGER mới được gọi API này
    @PostMapping
    @PreAuthorize("hasAuthority('ROLE_MANAGER')")
    public ApiResponse<ProjectEntity> createProject(@RequestBody ProjectEntity project) {
        return ApiResponse.<ProjectEntity>builder()
                .code(200)
                .message("Sếp (Manager) tạo Project thành công!")
                .result(projectService.createProject(project))
                .build();
    }
}
