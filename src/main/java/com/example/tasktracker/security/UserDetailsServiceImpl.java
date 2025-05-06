package com.example.tasktracker.security;


import com.example.tasktracker.dto.auth.AuthResponse;
import com.example.tasktracker.dto.auth.RegisterRequest;
import com.example.tasktracker.entity.User;
import com.example.tasktracker.entity.enums.Role;
import com.example.tasktracker.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

@Service
@AllArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {


    private UserRepository userRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));
        return new CustomUserDetails(user);
    }

    public boolean checkEmailUser(String email) {
        Optional<User> user = userRepository.findByEmail(email);
        boolean userExists = user.isPresent();

        if (userExists) {
            System.out.println("User already exists with email: " + email);
        } else {
            System.out.println("No user found with email: " + email);
        }

        return userExists;
    }
    @Transactional
    public AuthResponse register(RegisterRequest request) {
        Long userId = ThreadLocalRandom.current().nextLong(1, Long.MAX_VALUE);

        User user = User.builder()
                .role(Role.valueOf(request.getRole()))
                .password(passwordEncoder.encode(request.getPassword()))
                .email(request.getEmail())
                .id(userId)
                .build();

        userRepository.save(user);

        String jwtToken = jwtService.generateToken(new CustomUserDetails(user));

        return AuthResponse.builder()
                .token(jwtToken)
                .build();
    }

}
