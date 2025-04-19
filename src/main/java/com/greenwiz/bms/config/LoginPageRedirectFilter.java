package com.greenwiz.bms.config;

import com.greenwiz.bms.utils.JwtUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class LoginPageRedirectFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtils jwtUtils;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        
        // 只處理對 login.html 的請求
        if (request.getRequestURI().equals("/login.html")) {
            // 從 cookie 中獲取 JWT token
            Cookie[] cookies = request.getCookies();
            if (cookies != null) {
                for (Cookie cookie : cookies) {
                    if ("jwtToken".equals(cookie.getName())) {
                        String token = cookie.getValue();
                        // 如果 token 有效，重定向到 index.html
                        if (token != null && jwtUtils.validateToken(token)) {
                            response.sendRedirect("/page/admin/index.html");
                            return;
                        }
                        break;
                    }
                }
            }
        }
        
        filterChain.doFilter(request, response);
    }
}
