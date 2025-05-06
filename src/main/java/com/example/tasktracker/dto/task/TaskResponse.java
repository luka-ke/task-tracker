package com.example.tasktracker.dto.task;

import com.example.tasktracker.dto.user.UserSummary;
import com.example.tasktracker.dto.project.ProjectResponse;
import com.example.tasktracker.entity.enums.Priority;
import com.example.tasktracker.entity.enums.TaskStatus;
import lombok.Data;

import java.time.LocalDate;

@Data
public class TaskResponse {
    private Long id;
    private String title;
    private String description;
    private TaskStatus status;
    private Priority priority;
    private LocalDate dueDate;
    private ProjectResponse project;
    private UserSummary assignedUser;
}