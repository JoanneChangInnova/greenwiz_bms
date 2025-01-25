package com.greenwiz.bms.controller;

import com.greenwiz.bms.controller.data.auth.LoginRequest;
import com.greenwiz.bms.controller.data.auth.LoginResponse;
import com.greenwiz.bms.entity.User;
import com.greenwiz.bms.exception.BmsException;
import com.greenwiz.bms.service.UserService;
import com.greenwiz.bms.utils.JwtUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.Cookie;
import java.util.HashMap;
import java.util.Map;
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
        String token = jwtUtils.generateToken(user.getEmail(),
                Collections.singletonList(user.getRole().name()));

        // 將 Token 存入 HttpOnly Cookie
        Cookie cookie = new Cookie("jwtToken", token);
        cookie.setHttpOnly(true);  // 防止 JS 存取
        cookie.setPath("/");       // 在整個應用程序中可用
        cookie.setMaxAge(7 * 24 * 60 * 60); // 7 天有效期
        if (isSecure) {
            cookie.setSecure(true); //是否僅在 HTTPS 下傳輸
            cookie.setAttribute("SameSite", "None");
        }

        response.addCookie(cookie);

        // 返回成功響應
        return ResponseEntity.ok(new LoginResponse("登入成功", user.getEmail(), user.getRole().name()));
    }


    /**
     * 用戶登出接口
     */
    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request, HttpServletResponse response) {
        // 清除服務端會話
        request.getSession().invalidate();

        // 清除 JSESSIONID Cookie
        Cookie sessionCookie = new Cookie("JSESSIONID", null);
        sessionCookie.setPath("/"); // 匹配應用的 Cookie 路徑
        sessionCookie.setHttpOnly(true);
        sessionCookie.setMaxAge(0); // 設置立即過期
        if (isSecure) {
            sessionCookie.setSecure(true); //是否僅在 HTTPS 下傳輸
            sessionCookie.setAttribute("SameSite", "None");
        }
        response.addCookie(sessionCookie);

        // 清除 Cookie
        Cookie cookie = new Cookie("jwtToken", null);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        cookie.setMaxAge(0); // 立即過期
        if (isSecure) {
            cookie.setSecure(true); //是否僅在 HTTPS 下傳輸
            cookie.setAttribute("SameSite", "None");
        }
        response.addCookie(cookie);

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

    @GetMapping("/userInfo")
    public ResponseEntity<?> getCurrentUser(HttpServletRequest request) {
        // 從 HttpServletRequest 中提取 Cookie
        String token = jwtUtils.extractJwtFromCookie(request);
        if (token == null || !jwtUtils.validateToken(token)) {
            throw new BmsException("未登錄或 Token 無效");
        }

        // 從 JWT Token 中提取用戶資訊
        String email = jwtUtils.getEmailFromJwtToken(token);
        User user = userService.findByEmail(email);
        if (user == null) {
            throw new BmsException("不存在用戶");
        }

        // 返回用戶資訊
        Map<String, Object> userInfo = new HashMap<>();
        userInfo.put("userId", user.getId());
        userInfo.put("username", user.getUsername());
        userInfo.put("email", user.getEmail());
        userInfo.put("role", user.getRole());
        return ResponseEntity.ok(userInfo);
    }

}