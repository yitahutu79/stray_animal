package com.example.strayanimal.auth;

import com.example.strayanimal.user.User;
import com.example.strayanimal.user.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Locale;

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

    @PostMapping("/register")
    public ResponseEntity<LoginResponse> register(@RequestBody RegisterRequest request) {
        String username = safeTrim(request.getUsername());
        String password = request.getPassword() == null ? "" : request.getPassword().trim();

        if (username.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "用户名不能为空");
        }
        if (password.length() < 6) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "密码长度不能少于 6 位");
        }
        if (userRepository.existsByUsername(username)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "用户名已存在");
        }

        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        user.setRealName(safeTrim(request.getRealName()));
        user.setPhone(safeTrim(request.getPhone()));
        user.setEmail(safeTrim(request.getEmail()));
        user.setAddress(safeTrim(request.getAddress()));

        String requestedType = safeTrim(request.getUserType()).toUpperCase(Locale.ROOT);
        user.setUserType(requestedType.isEmpty() ? "ADOPTER" : requestedType);
        user.setStatus("ENABLED");

        User saved = userRepository.save(user);

        LoginResponse resp = new LoginResponse();
        resp.setUserId(saved.getId());
        resp.setUsername(saved.getUsername());
        resp.setUserType(saved.getUserType());
        return ResponseEntity.status(HttpStatus.CREATED).body(resp);
    }

    private String safeTrim(String value) {
        return value == null ? "" : value.trim();
    }

    public static class LoginRequest {
        private String username;
        private String password;

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }
    }

    public static class LoginResponse {
        private Long userId;
        private String username;
        private String userType;

        public Long getUserId() {
            return userId;
        }

        public void setUserId(Long userId) {
            this.userId = userId;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getUserType() {
            return userType;
        }

        public void setUserType(String userType) {
            this.userType = userType;
        }
    }

    public static class RegisterRequest {
        private String username;
        private String password;
        private String realName;
        private String phone;
        private String email;
        private String address;
        private String userType;

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public String getRealName() {
            return realName;
        }

        public void setRealName(String realName) {
            this.realName = realName;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getUserType() {
            return userType;
        }

        public void setUserType(String userType) {
            this.userType = userType;
        }
    }
}
