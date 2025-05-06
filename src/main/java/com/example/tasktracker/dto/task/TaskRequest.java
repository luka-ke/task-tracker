package com.example.tasktracker.dto.task;


import com.example.tasktracker.entity.enums.Priority;
import com.example.tasktracker.entity.enums.TaskStatus;
import lombok.Data;

import java.time.LocalDate;

@Data
public class TaskRequest {
    private String title;
    private String description;
    private TaskStatus status;
    private Priority priority;
    private LocalDate dueDate;
    private Long projectId;
    private Long assignedUserId;
}
