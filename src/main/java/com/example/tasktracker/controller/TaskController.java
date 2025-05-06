package com.example.tasktracker.controller;

import com.example.tasktracker.dto.task.TaskFilter;
import com.example.tasktracker.dto.task.TaskRequest;
import com.example.tasktracker.dto.task.TaskResponse;
import com.example.tasktracker.entity.enums.Priority;
import com.example.tasktracker.entity.enums.TaskStatus;
import com.example.tasktracker.exception.ResourceNotFoundException;
import com.example.tasktracker.service.serviceImpl.TaskServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/tasks")
@AllArgsConstructor
public class TaskController {


    private final TaskServiceImpl taskServiceImpl;

    @PostMapping
    public ResponseEntity<TaskResponse> createTask(@RequestBody TaskRequest taskRequest) throws ResourceNotFoundException {
        TaskResponse taskResponse = taskServiceImpl.createTask(taskRequest);
        return ResponseEntity.ok(taskResponse);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TaskResponse> getTask(@PathVariable Long id) throws ResourceNotFoundException {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        if (taskServiceImpl.canAccessTask(id, email)) {
            TaskResponse taskResponse = taskServiceImpl.getTask(id);
            return ResponseEntity.ok(taskResponse);
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

    @GetMapping
    public ResponseEntity<Page<TaskResponse>> getTasks(@RequestParam(required = false) TaskStatus status,
                                                       @RequestParam(required = false) Priority priority,
                                                       @RequestParam(required = false) Long assignedUserId,
                                                       Pageable pageable) {
        TaskFilter filter = new TaskFilter(status, priority, assignedUserId);
        Page<TaskResponse> taskResponses = taskServiceImpl.getTasks(filter, pageable);
        return ResponseEntity.ok(taskResponses);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TaskResponse> updateTask(@PathVariable Long id, @RequestBody TaskRequest taskRequest) throws ResourceNotFoundException {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        if (taskServiceImpl.canAccessTask(id, email)) {
            TaskResponse taskResponse = taskServiceImpl.updateTask(id, taskRequest);
            return ResponseEntity.ok(taskResponse);
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<TaskResponse> updateTaskStatus(@PathVariable Long id, @RequestBody TaskRequest taskRequest) throws ResourceNotFoundException {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        if (taskServiceImpl.isAssignedUser(id, email)) {
            TaskResponse taskResponse = taskServiceImpl.updateTaskStatus(id, taskRequest.getStatus());
            return ResponseEntity.ok(taskResponse);
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id) throws ResourceNotFoundException {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        if (taskServiceImpl.canAccessTask(id, email)) {
            taskServiceImpl.deleteTask(id);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }


    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @PatchMapping("/{taskId}/assign/{userId}")
    public ResponseEntity<TaskResponse> assignTaskToUser(
            @PathVariable Long taskId,
            @PathVariable Long userId) throws ResourceNotFoundException {
        TaskResponse taskResponse = taskServiceImpl.assignTaskToUser(taskId, userId);
        return ResponseEntity.ok(taskResponse);
    }
}
