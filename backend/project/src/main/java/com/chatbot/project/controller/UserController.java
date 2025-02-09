package com.chatbot.project.controller;

import com.chatbot.project.entity.User;
import com.chatbot.project.security.JwtUtil;
import com.chatbot.project.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:5173") // CORS 정책 추가
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/register")
    public Map<String, Object> register(@RequestBody Map<String, String> request) {
        Map<String, Object> response = new HashMap<>();
        try {
            String username = request.get("username");
            String password = request.get("password");

            // 기존 사용자 확인
            Optional<User> existingUser = userService.findByUsername(username);
            if (existingUser.isPresent()) {
                throw new IllegalArgumentException("이미 존재하는 아이디입니다.");
            }

            User user = userService.registerUser(username, password);
            response.put("message", "회원가입 성공");
            response.put("user", user);
        } catch (IllegalArgumentException e) {
            response.put("error", e.getMessage());
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
                    .orElseThrow(() -> new IllegalArgumentException("아이디 또는 비밀번호가 잘못되었습니다.")); // ✅ Optional 처리

            if (!passwordEncoder.matches(password, user.getPassword())) { // ✅ new 없이 의존성 주입된 passwordEncoder 사용
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
