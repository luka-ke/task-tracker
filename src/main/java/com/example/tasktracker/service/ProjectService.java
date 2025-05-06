package com.example.tasktracker.service;

import com.example.tasktracker.dto.project.ProjectRequest;
import com.example.tasktracker.dto.project.ProjectResponse;
import com.example.tasktracker.exception.ResourceNotFoundException;

public interface ProjectService {
    ProjectResponse createProject(ProjectRequest request) throws ResourceNotFoundException;
    ProjectResponse getProject(Long id) throws ResourceNotFoundException;
    ProjectResponse updateProject(Long id, ProjectRequest request) throws ResourceNotFoundException;
    void deleteProject(Long id) throws ResourceNotFoundException;
}
