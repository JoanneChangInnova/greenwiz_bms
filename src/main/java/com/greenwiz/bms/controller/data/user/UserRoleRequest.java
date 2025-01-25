package com.greenwiz.bms.controller.data.user;

import com.greenwiz.bms.enumeration.UserRole;
import lombok.Data;

@Data
public class UserRoleRequest {
    private UserRole userRole;
}