package com.greenwiz.bms.controller.data.user;

import lombok.Data;

/**
 * @author Johnny 2025/3/17
 */
@Data
public class ChangePasswordRequest {
    private String oldPassword;
    private String newPassword;

}
