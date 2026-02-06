package com.example.strayanimal.auth;

import com.example.strayanimal.user.User;
import com.example.strayanimal.user.UserRepository;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserRepository userRepository;

    public AuthController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {
        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "用户不存在"));

        String rawPassword = request.getPassword() == null ? "" : request.getPassword();
        String storedPassword = user.getPassword() == null ? "" : user.getPassword();

        // 简单明文密码校验：实际项目中应使用加密（如 BCrypt）
        if (!storedPassword.isEmpty() && !storedPassword.equals(rawPassword)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "密码错误");
        }

        LoginResponse resp = new LoginResponse();
        resp.setUserId(user.getId());
        resp.setUsername(user.getUsername());
        resp.setUserType(user.getUserType());
        return ResponseEntity.ok(resp);
    }

    @Data
    public static class LoginRequest {
        private String username;
        private String password;
    }

    @Data
    public static class LoginResponse {
        private Long userId;
        private String username;
        private String userType;
    }
}

