package com.greenwiz.bms.controller;

import com.greenwiz.bms.controller.data.auth.LoginRequest;
import com.greenwiz.bms.controller.data.auth.LoginResponse;
import com.greenwiz.bms.entity.User;
import com.greenwiz.bms.service.UserService;
import com.greenwiz.bms.utils.JwtUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;

/**
 * @author Johnny 2025/1/10
 */
@RestController
@RequestMapping("/api/v1/auth")
@Slf4j
public class AuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtils jwtUtils;

    /**
     * 用戶登錄接口
     *
     * @param loginRequest 登錄請求數據
     * @return JWT Token 和用戶信息
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        // 根據用戶名查詢用戶
        User user = userService.findByUsername(loginRequest.getUsername());
        if (user == null) {
            return ResponseEntity.badRequest().body("用戶名或密碼錯誤");
        }

        // 驗證密碼
        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            return ResponseEntity.badRequest().body("用戶名或密碼錯誤");
        }

        // 生成 JWT Token
        String token = jwtUtils.generateToken(user.getUsername(),
                Collections.singletonList(user.getRole().name()));

        // 返回成功響應
        return ResponseEntity.ok(new LoginResponse(token, user.getUsername(),
                user.getRole().name()));
    }

    /**
     * 用戶登出接口
     * 注意：如果使用前端存儲 Token，登出邏輯通常由前端直接清除 Token。
     */
    @PostMapping("/logout")
    public ResponseEntity<?> logout() {
        return ResponseEntity.ok("登出成功");
    }

    /**
     * 驗證 JWT 的有效性
     *
     * @param token JWT Token
     * @return 驗證結果
     */
    @PostMapping("/validate")
    public ResponseEntity<?> validateToken(@RequestParam String token) {
        if (jwtUtils.validateToken(token)) {
            String username = jwtUtils.getUserNameFromJwtToken(token);
            return ResponseEntity.ok("Token 有效，當前用戶：" + username);
        } else {
            return ResponseEntity.badRequest().body("Token 無效或已過期");
        }
    }
}