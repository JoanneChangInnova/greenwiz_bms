package com.greenwiz.bms.controller.data.auth;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author Johnny 2025/1/10
 */
@Data
@AllArgsConstructor
public class LoginResponse {
    private String token;
    private String username;
    private String role;
}
