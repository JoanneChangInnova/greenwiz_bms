package com.greenwiz.bms.controller;

import com.greenwiz.bms.controller.data.auth.LoginRequest;
import com.greenwiz.bms.controller.data.auth.LoginResponse;
import com.greenwiz.bms.entity.User;
import com.greenwiz.bms.exception.BmsException;
import com.greenwiz.bms.facade.UserFacade;
import com.greenwiz.bms.service.UserService;
import com.greenwiz.bms.utils.JwtUtils;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

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

    @Autowired
    private UserFacade userFacade;

    @Value("${server.secure.enabled:false}")
    private boolean isSecure;

    @Value("${auth.cookie.name:jwtToken}")
    private String jwtCookieName;

    /**
     * 用戶登錄接口
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest, HttpServletResponse response) {
        User user = userService.findByEmail(loginRequest.getEmail());

        // 避免洩露用戶是否存在
        boolean validUser = (user != null && passwordEncoder.matches(loginRequest.getPassword(), user.getPassword()));
        if (!validUser) {
            throw new BmsException("用戶名或密碼錯誤");
        }

        String token = jwtUtils.generateToken(user.getId(), Collections.singletonList(user.getRole().name()));

        // 設置 HttpOnly Cookie
        Cookie cookie = new Cookie(jwtCookieName, token);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        cookie.setMaxAge(7 * 24 * 60 * 60); // 7 天有效
        cookie.setSecure(isSecure);
        cookie.setAttribute("SameSite", isSecure ? "None" : "Lax");

        response.addCookie(cookie);
        return ResponseEntity.ok(new LoginResponse("登入成功", user.getEmail(), user.getRole().name()));
    }

    /**
     * 用戶登出接口
     */
    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request, HttpServletResponse response) {
        request.getSession().invalidate();

        // 清除 JSESSIONID Cookie
        clearCookie(response, "JSESSIONID");

        // 清除 JWT Token Cookie
        clearCookie(response, jwtCookieName);

        return ResponseEntity.ok("登出成功");
    }

    /**
     * 驗證 JWT Token 的有效性
     */
    @PostMapping("/validate")
    public ResponseEntity<?> validateToken(@RequestParam String token) {
        if (jwtUtils.validateToken(token)) {
            return ResponseEntity.ok("Token 有效");
        } else {
            throw new BmsException("Token 無效或已過期");
        }
    }

    /**
     * 獲取當前登入用戶資訊
     */
    @GetMapping("/userInfo")
    public ResponseEntity<?> getCurrentUser(@CookieValue(value = "jwtToken", required = false) String token) {
        if (token == null || !jwtUtils.validateToken(token)) {
            throw new BmsException("未登錄或 Token 無效");
        }

        Long id = jwtUtils.getUserIdFromJwtToken(token);
        User user = userService.findByPk(id);
        if (user == null) {
            throw new BmsException("不存在用戶");
        }

        Map<String, Object> userInfo = new HashMap<>();
        userInfo.put("userId", user.getId());
        userInfo.put("username", user.getUsername());
        userInfo.put("email", user.getEmail());
        userInfo.put("role", user.getRole());
        return ResponseEntity.ok(userInfo);
    }

    /**
     * 清除 Cookie
     */
    private void clearCookie(HttpServletResponse response, String cookieName) {
        Cookie cookie = new Cookie(cookieName, null);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        cookie.setMaxAge(0);
        cookie.setSecure(isSecure);
        cookie.setAttribute("SameSite", isSecure ? "None" : "Lax");

        response.addCookie(cookie);
    }

}
