package com.example.tasktracker.dto.project;

import com.example.tasktracker.dto.user.UserSummary;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ProjectResponse {
    private Long id;
    private String name;
    private String description;
    private UserSummary owner;
}