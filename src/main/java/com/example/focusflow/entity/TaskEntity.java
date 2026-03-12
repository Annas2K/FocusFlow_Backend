package com.example.focusflow.entity;

import com.example.focusflow.enums.TaskStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "tasks")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TaskEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TaskStatus status;
    private String priority; // LOW, MEDIUM, HIGH

    private LocalDateTime deadline;

    // Many-to-One: Nhiều Task thuộc về 1 User
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id") // Tên cột khóa ngoại trong MSSQL
    private UserEntity user;

    // Many-to-One: Nhiều Task thuộc về 1 Project
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id") // Tên cột khóa ngoại trong MSSQL
    private ProjectEntity project;
}
