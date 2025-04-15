package com.greenwiz.bms.controller.data.user;

import com.greenwiz.bms.controller.data.base.PageReq;
import com.greenwiz.bms.enumeration.Country;
import com.greenwiz.bms.enumeration.UserRole;
import com.greenwiz.bms.enumeration.UserState;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class ListUserReq extends PageReq {
    private String username;
    private String email;
    private UserRole role;
    private UserState state;
    private Country country;
}
