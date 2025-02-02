package com.chatbot.project.controller;

import com.chatbot.project.entity.User;
import com.chatbot.project.security.JwtUtil;
import com.chatbot.project.service.UserService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "http://localhost:5173")
public class AuthController {
    private final UserService userService;
    private final JwtUtil jwtUtil;

    public AuthController(UserService userService, JwtUtil jwtUtil) {
        this.userService = userService;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/register")
    public User register(@RequestBody Map<String, String> request) {
        return userService.registerUser(request.get("username"), request.get("password"));
    }

    @PostMapping("/login")
    public String login(@RequestBody Map<String, String> request) {
        Optional<User> user = userService.findByUsername(request.get("username"));
        if (user.isPresent() && new BCryptPasswordEncoder().matches(request.get("password"), user.get().getPassword())) {
            return jwtUtil.generateToken(user.get().getUsername());
        }
        return "Invalid credentials";
    }
}
