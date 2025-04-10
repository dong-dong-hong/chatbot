package com.chatbot.project.controller;

import com.chatbot.project.entity.User;
import com.chatbot.project.security.JwtUtil;
import com.chatbot.project.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "http://localhost:5173") // CORS 설정 추가
public class AuthController {
    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

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

    @GetMapping("/find-username")
    public ResponseEntity<Map<String, String>> findUsername(@RequestParam String email) {
        Optional<User> userOpt = userService.findByEmail(email);
        Map<String, String> response = new HashMap<>();
        if (userOpt.isPresent()) {
            response.put("username", userOpt.get().getUsername());
            return ResponseEntity.ok(response);
        } else {
            response.put("error", "해당 이메일로 등록된 계정이 없습니다.");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    @PostMapping("/find-password")
    public ResponseEntity<Map<String, String>> findPassword(@RequestBody Map<String, String> request) {
        String username = request.get("username");
        String email = request.get("email");
        Map<String, String> response = new HashMap<>();

        Optional<User> userOpt = userService.findByUsername(username);
        if (userOpt.isPresent() && userOpt.get().getEmail().equals(email)) {
            // 1. 임시 비밀번호 생성
            String tempPassword = UUID.randomUUID().toString().substring(0, 8); // 예: 8자리 임시비번

            // 2. 암호화해서 DB에 저장
            User user = userOpt.get();
            user.setPassword(passwordEncoder.encode(tempPassword));
            userService.save(user);

            // 실제 운영에선 이메일 전송 필요
            response.put("tempPassword", tempPassword);
            response.put("message", "임시 비밀번호가 발급되었습니다.");
            return ResponseEntity.ok(response);
        } else {
            response.put("error", "일치하는 계정을 찾을 수 없습니다.");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    @PostMapping("/change-password")
    public ResponseEntity<Map<String, String>> changePassword(
            @RequestHeader("Authorization") String authHeader,
            @RequestBody Map<String, String> request) {

        Map<String, String> response = new HashMap<>();

        try {
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                response.put("error", "유효하지 않은 토큰입니다.");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
            }

            String jwt = authHeader.substring(7); // Bearer 제거
            String username = jwtUtil.extractUsername(jwt); // token만 넘기기

            Optional<User> userOpt = userService.findByUsername(username);
            if (userOpt.isEmpty()) {
                response.put("error", "사용자를 찾을 수 없습니다.");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
            }

            User user = userOpt.get();
            String currentPassword = request.get("currentPassword");
            String newPassword = request.get("newPassword");

            if (currentPassword == null || newPassword == null) {
                response.put("error", "현재 비밀번호와 새 비밀번호를 모두 입력해주세요.");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }

            if (!passwordEncoder.matches(currentPassword, user.getPassword())) {
                response.put("error", "현재 비밀번호가 일치하지 않습니다.");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }

            user.setPassword(passwordEncoder.encode(newPassword));
            userService.save(user);

            response.put("message", "비밀번호가 성공적으로 변경되었습니다.");
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            logger.error("비밀번호 변경 중 오류 발생", e);
            response.put("error", "비밀번호 변경 중 오류 발생: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }


}
