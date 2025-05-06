package com.example.tasktracker.dto.project;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ProjectRequest {
    private String name;
    private String description;
}
