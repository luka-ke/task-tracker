package com.example.tasktracker.controller;

import com.example.tasktracker.dto.auth.AuthResponse;
import com.example.tasktracker.dto.auth.RegisterRequest;
import com.example.tasktracker.entity.User;
import com.example.tasktracker.entity.enums.Role;
import com.example.tasktracker.repository.UserRepository;
import com.example.tasktracker.security.CustomUserDetails;
import com.example.tasktracker.security.JwtService;
import com.example.tasktracker.security.UserDetailsServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/v1")
@AllArgsConstructor
public class AuthController {

    private final UserDetailsServiceImpl userService;

    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;

    @CrossOrigin
    @PostMapping("/public/api/users")
    public ResponseEntity<AuthResponse> register(@RequestBody RegisterRequest request) {
        System.out.println("Register request received for email: " + request.getEmail());

        if (userService.checkEmailUser(request.getEmail()) && !request.getRole().equals(Role.ADMIN.name())) {
            System.out.println("Email already exists: " + request.getEmail());
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }

        AuthResponse response = userService.register(request);
        return ResponseEntity.ok(response);
    }


    @CrossOrigin
    @GetMapping("/public/login")
    public ResponseEntity<AuthResponse> authenticateUser(@RequestParam String email, @RequestParam String password) {

        Optional<User> userOpt = userRepository.findByEmail(email);


        if (userOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new AuthResponse("Invalid email or password"));
        }

        User user = userOpt.get();

        boolean passwordMatches = passwordEncoder.matches(password, user.getPassword());
        if (!passwordMatches) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new AuthResponse("Invalid email or password"));
        }

        String jwtToken = jwtService.generateToken(new CustomUserDetails(user));


        return ResponseEntity.ok(new AuthResponse(jwtToken));
    }
}