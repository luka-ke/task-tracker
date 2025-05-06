package com.example.tasktracker.mapper;

import com.example.tasktracker.dto.task.TaskRequest;
import com.example.tasktracker.dto.task.TaskResponse;
import com.example.tasktracker.entity.Task;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {UserMapper.class, ProjectMapper.class})
public interface TaskMapper {

    @Mapping(source = "project", target = "project")
    @Mapping(source = "assignedUser", target = "assignedUser")
    TaskResponse toTaskResponse(Task task);

    Task toTask(TaskRequest request);
}