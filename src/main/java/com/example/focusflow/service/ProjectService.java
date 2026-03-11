package com.example.focusflow.service;

import com.example.focusflow.entity.ProjectEntity;
import com.example.focusflow.repository.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProjectService {

    @Autowired
    private ProjectRepository projectRepository;

    // Lấy danh sách tất cả Project
    public List<ProjectEntity> getAllProjects() {
        return projectRepository.findAll();
    }

    // Tạo Project mới
    public ProjectEntity createProject(ProjectEntity newProject) {
        return projectRepository.save(newProject);
    }

    // Tìm Project theo ID (Sau này nhét Task vào Project sẽ cần hàm này)
    public ProjectEntity getProjectById(Long id) {
        return projectRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Đéo tìm thấy Project có ID: " + id));
    }
}
