package com.example.focusflow.exception;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL) // Thằng nào null thì biến, không hiện ra JSON
public class ApiResponse<T> {
    private int code;
    private String message;
    private T result; // Đây chính là data (Task, List Task, Project...)
}
