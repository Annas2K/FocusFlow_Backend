package com.example.focusflow.service;

import com.example.focusflow.entity.ProjectEntity;
import com.example.focusflow.entity.TaskEntity;
import com.example.focusflow.entity.UserEntity;
import com.example.focusflow.repository.ProjectRepository;
import com.example.focusflow.repository.TaskRepository;
import com.example.focusflow.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import org.mockito.quality.Strictness;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class TaskServiceTest {

    @Mock private TaskRepository taskRepository;
    @Mock private UserRepository userRepository;
    @Mock private ProjectRepository projectRepository;

    @InjectMocks private TaskService taskService;

    // --- ĐỔI TÊN BIẾN CHO RÕ NGHĨA ---
    private UserEntity loggedInUser;
    private ProjectEntity activeProject;
    private TaskEntity validTaskRequest;

    @BeforeEach
    void setUp() {
        // 1. Giả lập đăng nhập
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(
                new UsernamePasswordAuthenticationToken("nagis_boss", "123")
        );
        SecurityContextHolder.setContext(securityContext);

        // 2. REFACTOR TRÙNG LẶP: Khởi tạo data dùng chung 1 lần ở đây thôi
        loggedInUser = new UserEntity();
        loggedInUser.setUsername("nagis_boss");

        activeProject = new ProjectEntity();
        activeProject.setId(1L);
        activeProject.setName("Dự án Leo Rank");

        validTaskRequest = new TaskEntity();
        validTaskRequest.setTitle("Gánh team Valorant");
        validTaskRequest.setProject(activeProject);
    }

    @Test
    void createTask_WhenValidInput_ReturnsSavedTask() {
        when(userRepository.findByUsername("nagis_boss")).thenReturn(Optional.of(loggedInUser));
        when(projectRepository.findById(1L)).thenReturn(Optional.of(activeProject));
        when(taskRepository.save(any(TaskEntity.class))).thenReturn(validTaskRequest);

        TaskEntity savedTask = taskService.createTask(validTaskRequest);

        assertNotNull(savedTask);
        verify(taskRepository, times(1)).save(any(TaskEntity.class));
    }

    @Test
    void createTask_WhenMissingProject_ThrowsException() {
        TaskEntity invalidTask = new TaskEntity();
        invalidTask.setTitle("Task lỗi");

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            taskService.createTask(invalidTask);
        });

        assertEquals("Lỗi: Tạo Task bắt buộc phải truyền kèm Project ID!", exception.getMessage());
        verify(taskRepository, never()).save(any(TaskEntity.class));
    }

    @Test
    void createTask_ShouldAssignToCurrentUser() {
        when(userRepository.findByUsername("nagis_boss")).thenReturn(Optional.of(loggedInUser));
        when(projectRepository.findById(1L)).thenReturn(Optional.of(activeProject));
        when(taskRepository.save(any(TaskEntity.class))).thenAnswer(i -> i.getArguments()[0]);

        ArgumentCaptor<TaskEntity> taskCaptor = ArgumentCaptor.forClass(TaskEntity.class);

        taskService.createTask(validTaskRequest);

        verify(taskRepository).save(taskCaptor.capture());
        assertEquals("nagis_boss", taskCaptor.getValue().getUser().getUsername());
    }
}
