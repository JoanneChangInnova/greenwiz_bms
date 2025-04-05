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

    private String displayText;

    // 在建構函數或 setter 方法中設置 displayText
    public void setDisplayText() {
        this.displayText = this.username + " (" + this.email + ")";
    }

    // 或者使用 getter 方法動態生成
    public String getDisplayText() {
        return this.username + " (" + this.email + ")";
    }
}
