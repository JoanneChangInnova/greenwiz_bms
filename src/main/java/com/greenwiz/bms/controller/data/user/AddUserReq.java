package com.greenwiz.bms.controller.data.user;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.experimental.Delegate;

@Data
public class AddUserReq {

    @Delegate
    @Valid
    private UserRequest userRequest = new UserRequest();

    /**
     * 明文密碼
     */
    @NotBlank(message = "密碼不能為空")
    private String password;
}
