package com.example.tasktracker.controller;

import com.example.tasktracker.dto.user.UserResponse;
import com.example.tasktracker.exception.ResourceNotFoundException;
import com.example.tasktracker.service.serviceImpl.UserServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@AllArgsConstructor
public class UserController {
    private final UserServiceImpl userServiceImpl;

    @GetMapping("/me")
    public ResponseEntity<UserResponse> getUserCurrentUser() throws ResourceNotFoundException {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        var user = userServiceImpl.getUserByEmail(email);
        return ResponseEntity.ok(user);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserResponse> getUserById(@PathVariable Long id) throws ResourceNotFoundException {
        return ResponseEntity.ok(userServiceImpl.getUserById(id));
    }


    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) throws ResourceNotFoundException {
        userServiceImpl.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

}