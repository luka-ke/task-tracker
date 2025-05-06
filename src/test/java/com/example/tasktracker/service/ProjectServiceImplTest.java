package com.example.tasktracker.service;

import com.example.tasktracker.dto.project.ProjectRequest;
import com.example.tasktracker.dto.project.ProjectResponse;
import com.example.tasktracker.entity.Project;
import com.example.tasktracker.entity.User;
import com.example.tasktracker.exception.ResourceNotFoundException;
import com.example.tasktracker.mapper.ProjectMapper;
import com.example.tasktracker.repository.ProjectRepository;
import com.example.tasktracker.repository.UserRepository;
import com.example.tasktracker.service.serviceImpl.ProjectServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProjectServiceImplTest {

    @Mock private ProjectRepository projectRepository;
    @Mock private UserRepository userRepository;
    @Mock private ProjectMapper projectMapper;
    @InjectMocks private ProjectServiceImpl projectServiceImpl;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createProject_success() throws ResourceNotFoundException {
        ProjectRequest request = new ProjectRequest("Test Project", "Desc");
        User mockUser = new User(); mockUser.setEmail("test@example.com");
        Project project = new Project(); project.setName("Test Project");
        Project savedProject = new Project(); savedProject.setId(1L);
        ProjectResponse response = new ProjectResponse(1L, "Test Project", "Desc", null);

        mockSecurityContext("test@example.com");
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(mockUser));
        when(projectRepository.save(any(Project.class))).thenReturn(savedProject);
        when(projectMapper.toProjectResponse(savedProject)).thenReturn(response);

        ProjectResponse result = projectServiceImpl.createProject(request);

        assertNotNull(result);
        assertEquals("Test Project", result.getName());
    }

    @Test
    void getProject_notFound() {
        when(projectRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> projectServiceImpl.getProject(1L));
    }


    private void mockSecurityContext(String email) {
        Authentication auth = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);
        when(auth.getName()).thenReturn(email);
        when(securityContext.getAuthentication()).thenReturn(auth);
        SecurityContextHolder.setContext(securityContext);
    }
}
