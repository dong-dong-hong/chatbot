package com.chatbot.project.controller;

import com.chatbot.project.entity.User;
import com.chatbot.project.security.JwtUtil;
import com.chatbot.project.service.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "http://localhost:5173") // CORS 설정 추가
public class AuthController {
    private final UserService userService;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;

    public AuthController(UserService userService, JwtUtil jwtUtil, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.jwtUtil = jwtUtil;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/register")
    public Map<String, Object> register(@RequestBody Map<String, String> request) {
        Map<String, Object> response = new HashMap<>();
        try {
            String username = request.get("username");
            String password = request.get("password");
            String email = request.get("email");
            String phoneNumber = request.get("phone_number");
            String role = request.get("role");

            // 기존 사용자 확인
            Optional<User> existingUser = userService.findByUsername(username);
            if (existingUser.isPresent()) {
                response.put("error", "이미 존재하는 아이디입니다.");
                return response;
            }

            User user = userService.registerUser(username, password, email, phoneNumber, role);
            response.put("message", "회원가입 성공");
            response.put("user", user);
        } catch (Exception e) {
            response.put("error", "회원가입 중 오류 발생: " + e.getMessage());
        }

        return response;
    }

    @PostMapping("/login")
    public Map<String, Object> login(@RequestBody Map<String, String> request) {
        Map<String, Object> response = new HashMap<>();
        String username = request.get("username");
        String password = request.get("password");

        try {
            User user = userService.findByUsername(username)
                    .orElseThrow(() -> new IllegalArgumentException("아이디 또는 비밀번호가 잘못되었습니다."));

            if (!passwordEncoder.matches(password, user.getPassword())) {
                throw new IllegalArgumentException("아이디 또는 비밀번호가 잘못되었습니다.");
            }

            String token = jwtUtil.generateToken(username);
            response.put("token", token);
            response.put("message", "로그인 성공");
        } catch (IllegalArgumentException e) {
            response.put("error", e.getMessage());
        }

        return response;
    }
}
