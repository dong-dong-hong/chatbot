package com.chatbot.project.controller;

import com.chatbot.project.entity.User;
import com.chatbot.project.security.JwtUtil;
import com.chatbot.project.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/register")
    public User register(@RequestBody Map<String, String> request) {
        return userService.registerUser(request.get("username"), request.get("password"));
    }

    @PostMapping("/login")
    public Map<String, String> login(@RequestBody Map<String, String> request) {
        String token = jwtUtil.generateToken(request.get("username"));
        Map<String, String> response = new HashMap<>();
        response.put("token", token);
        return response;
    }
}
