package com.chatbot.project.controller;

import com.chatbot.project.entity.User;
import com.chatbot.project.security.JwtUtil;
import com.chatbot.project.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLOutput;
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
            String email = request.get("email");
            String phoneNumber = request.get("phone_number");
            String role = request.get("role");

            // 기존 사용자 확인
            Optional<User> existingUser = userService.findByUsername(username);
            if (existingUser.isPresent()) {
                throw new IllegalArgumentException("이미 존재하는 아이디입니다.");
            }

            // 회원가입 처리
            User user = userService.registerUser(username, password, email, phoneNumber, role);
            response.put("message", "회원가입 성공");
            response.put("user", user);
        } catch (IllegalArgumentException e) {
            response.put("error", e.getMessage());
        }

        return response;
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody Map<String, String> request) {
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
            System.out.println("🚀 생성된 토큰: " + token);
            response.put("token", token);
            response.put("message", "로그인 성공");

            return ResponseEntity.ok(response);

        } catch (IllegalArgumentException e) {
            response.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }
    }

    @GetMapping("/check-username")
    public ResponseEntity<Map<String, Object>> checkUsername(@RequestParam String username) {
        Map<String, Object> response = new HashMap<>();
        Optional<User> existingUser = userService.findByUsername(username);

        if (existingUser.isPresent()) {
            response.put("available", false);
            response.put("message", "이미 사용 중인 아이디입니다.");
        } else {
            response.put("available", true);
            response.put("message", "사용 가능한 아이디입니다.");
        }

        return ResponseEntity.ok(response);
    }


}
