package com.greenwiz.bms.controller;

import com.greenwiz.bms.controller.data.auth.LoginRequest;
import com.greenwiz.bms.controller.data.auth.LoginResponse;
import com.greenwiz.bms.entity.User;
import com.greenwiz.bms.exception.BmsException;
import com.greenwiz.bms.service.UserService;
import com.greenwiz.bms.utils.JwtUtils;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.Cookie;


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

    @Value("${server.secure.enabled:false}") // 默認為 false
    private boolean isSecure;

    /**
     * 用戶登錄接口
     *
     * @param loginRequest 登錄請求數據
     * @return JWT Token 和用戶信息
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest, HttpServletResponse response) {
        // 根據用戶名查詢用戶
        User user = userService.findByEmail(loginRequest.getEmail());
        if (user == null) {
            throw new BmsException("用戶名或密碼錯誤");
        }

        // 驗證密碼
        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            throw new BmsException("用戶名或密碼錯誤");
        }

        // 生成 JWT Token
        String token = jwtUtils.generateToken(user.getUsername(),
                Collections.singletonList(user.getRole().name()));

        // 將 Token 存入 HttpOnly Cookie
        Cookie cookie = new Cookie("jwtToken", token);
        cookie.setHttpOnly(true);  // 防止 JS 存取
        cookie.setPath("/");       // 在整個應用程序中可用
        cookie.setMaxAge(7 * 24 * 60 * 60); // 7 天有效期

        if (isSecure) {
            cookie.setSecure(true); //是否僅在 HTTPS 下傳輸
        }

        response.addCookie(cookie);

        // 返回成功響應
        return ResponseEntity.ok(new LoginResponse("登入成功", user.getUsername(), user.getRole().name()));
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
            String email = jwtUtils.getEmailFromJwtToken(token);
            return ResponseEntity.ok("Token 有效，當前用戶：" + email);
        } else {
            throw new BmsException("Token 無效或已過期");
        }
    }
}