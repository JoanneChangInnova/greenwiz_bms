package com.greenwiz.bms.utils;

import java.security.Key;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import lombok.extern.slf4j.Slf4j;


/**
 * @author Johnny 2024/12/29
 */
@Component
@Slf4j
public class JwtUtils {

    @Value("${jwt.secret}")
    private String secret;

    private static final int JWT_EXPIRATION_MS = 86400000; // 24 hours

    /**
     * 生成簽名密鑰
     *
     * @return Key
     */
    private Key getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secret);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    /**
     * 生成 JWT Token
     *
     * @param email Email
     * @param roles 角色列表
     * @return JWT Token
     */
    public String generateToken(String email, List<String> roles) {
        return Jwts.builder()
                .setSubject(email)
                .claim("roles", roles)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + JWT_EXPIRATION_MS))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * 從 JWT Token 中解析email
     *
     * @param token JWT Token
     * @return email
     */
    public String getEmailFromJwtToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    /**
     * 從 JWT Token 中解析角色
     *
     * @param token JWT Token
     * @return 角色列表
     */
    public List<SimpleGrantedAuthority> getRolesFromJwtToken(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();

        List<String> roles = claims.get("roles", List.class);
        return roles.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }

    /**
     * 驗證 JWT Token 的合法性
     *
     * @param token JWT Token
     * @return 是否有效
     */
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (JwtException e) {
            log.info("Invalid JWT: {}", e.getMessage());
        }
        return false;
    }

    public String extractJwtFromCookie(HttpServletRequest request) {
        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if ("jwtToken".equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }
}
