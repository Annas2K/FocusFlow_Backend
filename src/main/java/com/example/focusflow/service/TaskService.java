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

    // ĐÂY CHÍNH LÀ MỤC 3 & 4: IMPLEMENT TẠO TASK + VALIDATE PROJECT ID
    public TaskEntity createTask(TaskEntity newTask) {
        // 1. Kiểm tra xem payload gửi lên có chứa Project ID không
        if (newTask.getProject() == null || newTask.getProject().getId() == null) {
            throw new RuntimeException("Lỗi: Tạo Task bắt buộc phải truyền kèm Project ID!");
        }

        // ĐÂY LÀ MỤC 3: CUSTOM RULE - VALIDATE DEADLINE > CURRENT DATE
        if (newTask.getDeadline() != null) {
            // Lấy thời gian hiện tại của hệ thống
            LocalDateTime now = LocalDateTime.now();
            if (newTask.getDeadline().isBefore(now)) {
                throw new AppException(400, "Deadline không thể nằm trong quá khứ được! Mày định bắt nhân viên du hành thời gian à?");
            }
        }

        Long projectId = newTask.getProject().getId();

        // 2. Validate Project ID tồn tại
        ProjectEntity project = projectRepository.findById(projectId)
                .orElseThrow(() -> new RuntimeException("Đéo tìm thấy Project nào có ID: " + projectId));

        // 3. Ép luật (Business Rule): Task mới tạo mặc định phải là TODO
        if (newTask.getStatus() == null) {
            newTask.setStatus(TaskStatus.TODO);
        }

        // 4. Gắn lại project chuẩn từ DB vào để an toàn tuyệt đối
        newTask.setProject(project);

        // Lưu vào DB
        return taskRepository.save(newTask);
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

        // 2. Thay vì trả về hết, mày chỉ tìm những Task thuộc về username này
        // Mày cần viết thêm hàm findByUserUsername trong TaskRepository nhé
        return taskRepository.findByUserUsername(currentUsername);
    }


}
