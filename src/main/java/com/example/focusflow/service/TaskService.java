package com.example.focusflow.service;

import com.example.focusflow.entity.ProjectEntity;
import com.example.focusflow.entity.TaskEntity;
import com.example.focusflow.entity.UserEntity;
import com.example.focusflow.enums.TaskStatus;
import com.example.focusflow.exception.AppException;
import com.example.focusflow.repository.ProjectRepository;
import com.example.focusflow.repository.TaskRepository;
import com.example.focusflow.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class TaskService {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private UserRepository userRepository;

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

    // ĐÂY CHÍNH LÀ MỤC 3 & 4:
    public TaskEntity createTask(TaskEntity task) {
        // 1. Kiểm tra Project
        if (task.getProject() == null || task.getProject().getId() == null) {
            throw new RuntimeException("Lỗi: Tạo Task bắt buộc phải truyền kèm Project ID!");
        }

        // 2. Lấy thằng đang cầm Token gọi API
        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        // 3. Tìm thằng User đó trong DB
        UserEntity currentUser = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("không tìm thấy User!"));

        // 4. Gán chủ sở hữu cho task
        task.setUser(currentUser);

        // 5. Lưu xuống DB
        return taskRepository.save(task);
    }

    // ĐÂY LÀ MỤC 5 & 6: ASSIGN TASK VÀ CHECK USER THUỘC PROJECT
    public TaskEntity assignTask(Long taskId, Long userId) {
        // Tìm Task
        TaskEntity task = taskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Đéo tìm thấy Task ID: " + taskId));

        // Tìm User
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Đéo tìm thấy User ID: " + userId));

        ProjectEntity project = task.getProject();

        // MỤC 6: Kiểm tra User có nằm trong dự án không
        // Lấy danh sách thành viên của dự án ra check
        if (project.getMembers() == null || !project.getMembers().contains(user)) {
            throw new RuntimeException("Cảnh báo: Thằng user này đéo nằm trong dự án! Cấm giao việc!");
        }

        // MỤC 5: Pass hết các bài test thì mới được giao việc (Assign)
        task.setUser(user);

        // Lưu lại xuống DB
        return taskRepository.save(task);
    }

    // MỤC 7 & 8: UPDATE STATUS VÀ CHẶN NẾU TASK ĐÃ DONE
    public TaskEntity updateTaskStatus(Long taskId, TaskStatus newStatus) {
        // 1. Lôi thằng Task từ dưới DB lên
        TaskEntity task = taskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Đéo tìm thấy Task ID: " + taskId));

        // 2. MỤC 8: "BẢO VỆ" - Kiểm tra trạng thái HIỆN TẠI của Task
        // Nếu nó đang là DONE rồi thì ném thẳng cái ngoại lệ vào mặt thằng gọi API
        if (task.getStatus() == TaskStatus.DONE) {
            throw new RuntimeException("Lỗi Rule: Task này đã DONE rồi, hệ thống đã đóng băng, cấm sửa đổi!");
        }

        // 3. MỤC 7: Vượt qua được bảo vệ thì cho phép cập nhật trạng thái mới
        task.setStatus(newStatus);

        // Lưu lại xuống DB
        return taskRepository.save(task);
    }
    public List<TaskEntity> getMyTasks() {
        // 1. Lấy username của thằng đang cầm Token gọi API
        String currentUsername = SecurityContextHolder.getContext().getAuthentication().getName();

        // 2. Thay vì trả về hết chỉ tìm những Task thuộc về username này
        return taskRepository.findByUserUsername(currentUsername);
    }


}
