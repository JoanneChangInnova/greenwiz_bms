package com.greenwiz.bms.controller.data.user;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.experimental.Delegate;

import java.time.LocalDateTime;

@Data
public class UpdateUserReq {

    @NotNull(message = "id")
    private Long id;

    @Delegate
    @Valid
    private UserRequest userRequest = new UserRequest();

    @NotNull(message = "dtModify")
    private LocalDateTime dtModify;


}
