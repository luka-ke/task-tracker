package com.example.tasktracker.dto.task;


import com.example.tasktracker.entity.enums.Priority;
import com.example.tasktracker.entity.enums.TaskStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaskFilter {
    private TaskStatus status;
    private Priority priority;
    private Long assignedUserId;
}
