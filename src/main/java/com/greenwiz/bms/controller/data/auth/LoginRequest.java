package com.greenwiz.bms.controller.data.auth;

import lombok.Data;

/**
 * @author Johnny 2025/1/10
 */
@Data
public class LoginRequest {
    private String email;
    private String password;
}
