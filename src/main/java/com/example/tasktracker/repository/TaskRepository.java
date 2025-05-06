package com.example.tasktracker.repository;


import com.example.tasktracker.entity.Task;
import com.example.tasktracker.entity.User;
import com.example.tasktracker.entity.Project;
import com.example.tasktracker.entity.enums.TaskStatus;
import com.example.tasktracker.entity.enums.Priority;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long>, JpaSpecificationExecutor<Task> {
    List<Task> findByProject(Project project);
    List<Task> findByAssignedUser(User assignedUser);
    Page<Task> findByProjectAndStatus(Project project, TaskStatus status, Pageable pageable);
    Page<Task> findByAssignedUserAndStatus(User assignedUser, TaskStatus status, Pageable pageable);
    Page<Task> findByPriority(Priority priority, Pageable pageable);
    Page<Task> findByAssignedUserAndPriority(User assignedUser, Priority priority, Pageable pageable);
    Page<Task> findByAssignedUserId(Long userId, Pageable pageable);
}
