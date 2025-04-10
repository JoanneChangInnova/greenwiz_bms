package com.greenwiz.bms.controller.data.kraken;

import com.greenwiz.bms.enumeration.UserRole;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class AssignKrakenReq {
    @NotEmpty(message = "Kraken ID 不能為空")
    private List<Long> krakenIds;

    @NotNull(message = "派發對象 ID 不能為空")
    private Long userId;

    @NotNull(message = "派發對象角色 不能為空")
    private UserRole userRole;
}
