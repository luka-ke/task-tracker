package com.example.tasktracker.service.serviceImpl;

import com.example.tasktracker.dto.task.TaskFilter;
import com.example.tasktracker.dto.task.TaskRequest;
import com.example.tasktracker.dto.task.TaskResponse;
import com.example.tasktracker.dto.user.UserResponse;
import com.example.tasktracker.entity.Project;
import com.example.tasktracker.entity.Task;
import com.example.tasktracker.entity.User;
import com.example.tasktracker.entity.enums.Role;
import com.example.tasktracker.entity.enums.TaskStatus;
import com.example.tasktracker.exception.ResourceNotFoundException;
import com.example.tasktracker.mapper.TaskMapper;
import com.example.tasktracker.repository.ProjectRepository;
import com.example.tasktracker.repository.TaskRepository;
import com.example.tasktracker.repository.UserRepository;
import com.example.tasktracker.service.TaskService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {
    private final TaskRepository taskRepository;
    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;
    private final UserServiceImpl userService;
    private final TaskMapper taskMapper;
    @Transactional
    public TaskResponse createTask(TaskRequest request) throws ResourceNotFoundException {
        Project project = projectRepository.findById(request.getProjectId())
                .orElseThrow(() -> new ResourceNotFoundException("Project not found with id: " + request.getProjectId()));

        User assignedUser = null;
        if (request.getAssignedUserId() != null) {
            assignedUser = userRepository.findById(request.getAssignedUserId())
                    .orElseThrow(() -> new ResourceNotFoundException("Assigned user not found with id: " + request.getAssignedUserId()));
        }

        Task task = taskMapper.toTask(request);
        task.setProject(project);
        task.setAssignedUser(assignedUser);

        Task saved = taskRepository.save(task);
        return taskMapper.toTaskResponse(saved);
    }

    public TaskResponse getTask(Long id) throws ResourceNotFoundException {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found with id: " + id));
        return taskMapper.toTaskResponse(task);
    }


    public Page<TaskResponse> getTasks(TaskFilter filter, Pageable pageable) {
        Specification<Task> spec = Specification.where(null);

        if (filter.getStatus() != null) {
            spec = spec.and((root, query, cb) -> cb.equal(root.get("status"), filter.getStatus()));
        }
        if (filter.getPriority() != null) {
            spec = spec.and((root, query, cb) -> cb.equal(root.get("priority"), filter.getPriority()));
        }
        if (filter.getAssignedUserId() != null) {
            spec = spec.and((root, query, cb) -> cb.equal(root.get("assignedUser").get("id"), filter.getAssignedUserId()));
        }

        return taskRepository.findAll(spec, pageable)
                .map(taskMapper::toTaskResponse);
    }
    @Transactional
    public TaskResponse updateTask(Long id, TaskRequest request) throws ResourceNotFoundException {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found with id: " + id));

        task.setTitle(request.getTitle());
        task.setDescription(request.getDescription());
        task.setStatus(request.getStatus());
        task.setPriority(request.getPriority());
        task.setDueDate(request.getDueDate());

        if (request.getAssignedUserId() != null) {
            User assignedUser = userRepository.findById(request.getAssignedUserId())
                    .orElseThrow(() -> new ResourceNotFoundException("Assigned user not found with id: " + request.getAssignedUserId()));
            task.setAssignedUser(assignedUser);
        } else {
            task.setAssignedUser(null);
        }

        if (request.getProjectId() != null && !request.getProjectId().equals(task.getProject().getId())) {
            Project project = projectRepository.findById(request.getProjectId())
                    .orElseThrow(() -> new ResourceNotFoundException("Project not found with id: " + request.getProjectId()));
            task.setProject(project);
        }

        Task updated = taskRepository.save(task);
        return taskMapper.toTaskResponse(updated);
    }
    @Transactional
    public TaskResponse updateTaskStatus(Long id, TaskStatus status) throws ResourceNotFoundException {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found with id: " + id));
        task.setStatus(status);
        return taskMapper.toTaskResponse(taskRepository.save(task));
    }

    @Transactional
    public void deleteTask(Long id) throws ResourceNotFoundException {
        if (!taskRepository.existsById(id)) {
            throw new ResourceNotFoundException("Task not found with id: " + id);
        }
        taskRepository.deleteById(id);
    }
    @Transactional
    public TaskResponse assignTaskToUser(Long taskId, Long userId) throws ResourceNotFoundException {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found with id: " + taskId));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));

        task.setAssignedUser(user);
        taskRepository.save(task);

        return taskMapper.toTaskResponse(task);
    }

    public boolean canAccessTask(Long taskId, String currentUserEmail) throws ResourceNotFoundException {
        UserResponse currentUser = userService.getUserByEmail(currentUserEmail);
        TaskResponse task = getTask(taskId);
        if(currentUser.getRole().equals(Role.MANAGER) && !task.getProject().getOwner().getId().equals(currentUser.getId()) ){
            return false;
        }
        return currentUser.getId().equals(task.getAssignedUser().getId()) ||
                currentUser.getId().equals(task.getProject().getOwner().getId()) ||  currentUser.getRole().equals(Role.ADMIN);
    }

    public boolean isAssignedUser(Long taskId, String currentUserEmail) throws ResourceNotFoundException {
        UserResponse currentUser = userService.getUserByEmail(currentUserEmail);
        TaskResponse task = getTask(taskId);

        return currentUser.getId().equals(task.getAssignedUser().getId());
    }

}
