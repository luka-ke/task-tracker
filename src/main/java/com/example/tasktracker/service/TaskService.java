package com.example.tasktracker.service;

import com.example.tasktracker.dto.task.TaskFilter;
import com.example.tasktracker.dto.task.TaskRequest;
import com.example.tasktracker.dto.task.TaskResponse;
import com.example.tasktracker.entity.enums.TaskStatus;
import com.example.tasktracker.exception.ResourceNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface TaskService {
    TaskResponse createTask(TaskRequest request) throws ResourceNotFoundException;
    TaskResponse getTask(Long id) throws ResourceNotFoundException;
    Page<TaskResponse> getTasks(TaskFilter filter, Pageable pageable);
    TaskResponse updateTask(Long id, TaskRequest request) throws ResourceNotFoundException;
    TaskResponse updateTaskStatus(Long id, TaskStatus status) throws ResourceNotFoundException;
    void deleteTask(Long id) throws ResourceNotFoundException;
    TaskResponse assignTaskToUser(Long taskId, Long userId) throws ResourceNotFoundException;
    boolean canAccessTask(Long taskId, String currentUserEmail) throws ResourceNotFoundException;
    boolean isAssignedUser(Long taskId, String currentUserEmail) throws ResourceNotFoundException;
}
