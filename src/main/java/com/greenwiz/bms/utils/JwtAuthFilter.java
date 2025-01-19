package com.greenwiz.bms.utils;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

/**
 * @author Johnny 2024/12/29
 */
@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtUtils jwtUtils;

    public JwtAuthFilter(JwtUtils jwtUtils) {
        this.jwtUtils = jwtUtils;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        // 從 Cookie 中提取 JWT Token
        String token = jwtUtils.extractJwtFromCookie(request);

        if (token == null) {
            filterChain.doFilter(request, response); // 無 Token，放行至下一過濾器
            return;
        }

        try {
            // 驗證 Token
            if (jwtUtils.validateToken(token)) {
                // 從 Token 中提取用戶信息
                String email = jwtUtils.getEmailFromJwtToken(token);

                // 從 Token 獲取角色，轉換為 Spring Security 的 GrantedAuthority
                List<SimpleGrantedAuthority> roles = jwtUtils.getRolesFromJwtToken(token);

                // 創建認證對象並設置到 SecurityContext
                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(email, null, roles);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            } else {
                logger.info("無效的 JWT Token");
            }
        } catch (Exception e) {
            logger.error("處理 JWT 時發生錯誤: {}", e);
        }

        // 繼續過濾器鏈
        filterChain.doFilter(request, response);
    }

}