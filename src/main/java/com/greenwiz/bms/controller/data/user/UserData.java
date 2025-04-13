package com.greenwiz.bms.controller.data.user;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserData {
    private Long id;
    private String username;
    private String email;

    // 或者使用 getter 方法動態生成
    public String getDisplayText() {
        return this.username + " (" + this.email + ")";
    }
}
