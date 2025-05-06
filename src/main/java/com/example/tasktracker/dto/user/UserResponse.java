package com.example.tasktracker.dto.user;

import com.example.tasktracker.entity.enums.Role;
import lombok.Data;

@Data
public class UserResponse {
    private Long id;
    private String email;
    private Role role;
}