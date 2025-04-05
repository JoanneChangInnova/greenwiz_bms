package com.greenwiz.bms.controller.data.user;

import lombok.Data;

@Data
public class CreateModifyUser {

    /**
     * createUser userName + (email)
     */
    private String createUserInfo = "";

    /**
     * modifyUser userName + (email)
     */
    private String modifyUserInfo = "";
}
