package com.example.tasktracker.service.serviceImpl;

import com.example.tasktracker.dto.project.ProjectRequest;
import com.example.tasktracker.dto.project.ProjectResponse;
import com.example.tasktracker.dto.user.UserResponse;
import com.example.tasktracker.entity.Project;
import com.example.tasktracker.entity.User;
import com.example.tasktracker.entity.enums.Role;
import com.example.tasktracker.exception.ResourceNotFoundException;
import com.example.tasktracker.mapper.ProjectMapper;
import com.example.tasktracker.repository.ProjectRepository;
import com.example.tasktracker.repository.UserRepository;
import com.example.tasktracker.service.ProjectService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProjectServiceImpl implements ProjectService {
    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;
    private final ProjectMapper projectMapper;
    private final UserServiceImpl userService;
    @Transactional
    public ProjectResponse createProject(ProjectRequest request) throws ResourceNotFoundException {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        User owner = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Owner not found with email: " + email));

        Project project = new Project();
        project.setName(request.getName());
        project.setDescription(request.getDescription());
        project.setOwner(owner);

        Project saved = projectRepository.save(project);
        return projectMapper.toProjectResponse(saved);
    }

    public ProjectResponse getProject(Long id) throws ResourceNotFoundException {
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found with id: " + id));
        return projectMapper.toProjectResponse(project);
    }

    @Transactional
    public ProjectResponse updateProject(Long id, ProjectRequest request) throws ResourceNotFoundException {
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found with id: " + id));

        project.setName(request.getName());
        project.setDescription(request.getDescription());

        Project updated = projectRepository.save(project);
        return projectMapper.toProjectResponse(updated);
    }
    @Transactional
    public void deleteProject(Long id) throws ResourceNotFoundException {
        if (!projectRepository.existsById(id)) {
            throw new ResourceNotFoundException("Project not found with id: " + id);
        }
        projectRepository.deleteById(id);
    }

    public boolean canAccessProject(Long projectId, String currentUserEmail) throws ResourceNotFoundException {
        UserResponse currentUser = userService.getUserByEmail(currentUserEmail);
        ProjectResponse project = getProject(projectId);

        return currentUser.getId().equals(project.getOwner().getId()) || currentUser.getRole().equals(Role.ADMIN);
    }
}
