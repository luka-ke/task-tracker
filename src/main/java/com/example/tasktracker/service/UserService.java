package com.example.tasktracker.service;

import com.example.tasktracker.dto.user.UserResponse;
import com.example.tasktracker.exception.ResourceNotFoundException;

public interface UserService {
    UserResponse getUserByEmail(String email) throws ResourceNotFoundException;
    UserResponse getUserById(Long id) throws ResourceNotFoundException;
    void deleteUser(Long id) throws ResourceNotFoundException;
}
