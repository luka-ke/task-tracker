package com.example.tasktracker.service;

import com.example.tasktracker.dto.task.TaskRequest;
import com.example.tasktracker.dto.task.TaskResponse;
import com.example.tasktracker.entity.Project;
import com.example.tasktracker.entity.Task;
import com.example.tasktracker.entity.User;
import com.example.tasktracker.mapper.TaskMapper;
import com.example.tasktracker.repository.ProjectRepository;
import com.example.tasktracker.repository.TaskRepository;
import com.example.tasktracker.repository.UserRepository;
import com.example.tasktracker.exception.ResourceNotFoundException;
import com.example.tasktracker.service.serviceImpl.TaskServiceImpl;
import com.example.tasktracker.service.serviceImpl.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TaskServiceImplTest {

    @Mock private TaskRepository taskRepository;
    @Mock private ProjectRepository projectRepository;
    @Mock private UserRepository userRepository;
    @Mock private TaskMapper taskMapper;
    @Mock private UserServiceImpl userServiceImpl;
    @InjectMocks private TaskServiceImpl taskServiceImpl;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createTask_success() throws ResourceNotFoundException {
        TaskRequest request = new TaskRequest();
        request.setTitle("Title");
        request.setProjectId(1L);

        Project project = new Project(); project.setId(1L);
        Task task = new Task(); task.setTitle("Title");
        Task savedTask = new Task(); savedTask.setId(1L);
        TaskResponse response = new TaskResponse(); response.setId(1L);

        when(projectRepository.findById(1L)).thenReturn(Optional.of(project));
        when(taskMapper.toTask(request)).thenReturn(task);
        when(taskRepository.save(task)).thenReturn(savedTask);
        when(taskMapper.toTaskResponse(savedTask)).thenReturn(response);

        TaskResponse result = taskServiceImpl.createTask(request);
        assertEquals(1L, result.getId());
    }

    @Test
    void getTask_notFound() {
        when(taskRepository.findById(100L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> taskServiceImpl.getTask(100L));
    }

    @Test
    void deleteTask_notFound() {
        when(taskRepository.existsById(100L)).thenReturn(false);
        assertThrows(ResourceNotFoundException.class, () -> taskServiceImpl.deleteTask(100L));
    }

    @Test
    void assignTaskToUser_success() throws ResourceNotFoundException {
        Task task = new Task(); task.setId(1L);
        User user = new User(); user.setId(2L);
        TaskResponse response = new TaskResponse(); response.setId(1L);

        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));
        when(userRepository.findById(2L)).thenReturn(Optional.of(user));
        when(taskMapper.toTaskResponse(task)).thenReturn(response);

        TaskResponse result = taskServiceImpl.assignTaskToUser(1L, 2L);
        assertEquals(1L, result.getId());
    }
}
