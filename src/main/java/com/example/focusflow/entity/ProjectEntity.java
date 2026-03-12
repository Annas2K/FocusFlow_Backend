package com.example.focusflow.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "projects")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProjectEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    private String description;

    @Column(name = "start_date")
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    // Mapping 1-N: Một Project có nhiều Task
    // mappedBy = "project" nghĩa là biến 'project' bên bảng Task sẽ giữ khóa ngoại
    @JsonIgnore
    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<TaskEntity> tasks;
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "project_members", // Hibernate sẽ tự đẻ ra bảng trung gian này
            joinColumns = @JoinColumn(name = "project_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    @JsonIgnore // Nhớ cái bùa chống lặp vô tận này không?
    private List<UserEntity> members;
}
