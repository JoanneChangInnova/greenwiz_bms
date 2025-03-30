package com.greenwiz.bms.controller.data.user;

import lombok.Data;

@Data
public class UserData {
    private Long id;
    private String username;
    private String email;

    public UserData(Long id, String username, String email) {
        this.id = id;
        this.username = username;
        this.email = email;
    }
}
