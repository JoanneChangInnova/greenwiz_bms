package com.greenwiz.bms.controller.data.user;

import lombok.Data;

@Data
public class UserListData extends BaseUser {

    /**
     * 父帳號 userName + (email)
     */
    private String parentUserInfo;

}
