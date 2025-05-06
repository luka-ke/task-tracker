package com.example.tasktracker.controller;


import com.example.tasktracker.dto.project.ProjectRequest;
import com.example.tasktracker.dto.project.ProjectResponse;
import com.example.tasktracker.exception.ResourceNotFoundException;
import com.example.tasktracker.service.serviceImpl.ProjectServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/projects")
@AllArgsConstructor
public class ProjectController {

    private final ProjectServiceImpl projectServiceImpl;
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @PostMapping
    public ResponseEntity<ProjectResponse> createProject(@RequestBody ProjectRequest projectRequest) throws ResourceNotFoundException {
        ProjectResponse projectResponse = projectServiceImpl.createProject(projectRequest);
        return ResponseEntity.ok(projectResponse);
    }
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @GetMapping("/{id}")
    public ResponseEntity<ProjectResponse> getProject(@PathVariable Long id) throws ResourceNotFoundException {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        if (projectServiceImpl.canAccessProject(id, email)) {
            ProjectResponse projectResponse = projectServiceImpl.getProject(id);
            return ResponseEntity.ok(projectResponse);
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @PutMapping("/{id}")
    public ResponseEntity<ProjectResponse> updateProject(@PathVariable Long id, @RequestBody ProjectRequest projectRequest) throws ResourceNotFoundException {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        if (projectServiceImpl.canAccessProject(id, email)) {
            ProjectResponse projectResponse = projectServiceImpl.updateProject(id, projectRequest);
            return ResponseEntity.ok(projectResponse);
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProject(@PathVariable Long id) throws ResourceNotFoundException {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        if (projectServiceImpl.canAccessProject(id, email)) {
            projectServiceImpl.deleteProject(id);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }
}
