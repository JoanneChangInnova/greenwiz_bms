package com.greenwiz.bms.controller.data.kraken;

import com.greenwiz.bms.enumeration.KrakenState;
import com.greenwiz.bms.enumeration.UserRole;
import com.greenwiz.bms.utils.ThreadLocalUtils;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
public class AddKrakenReq {
    private Long factoryId;

    /**
     * 新增Kraken的管理員預設即Kraken擁有者
     */
    private Long userId = ThreadLocalUtils.getUser().getId();

    private UserRole userRole = ThreadLocalUtils.getUser().getRole();

    @NotBlank(message = "型號別不能為空")
    private String krakenModel;

    @NotNull(message = "產品序號不能為空")
    private Integer factoryIotSerial;

    private String name;

    @NotNull(message = "狀態不能為空")
    private KrakenState state = KrakenState.STOCKED;

    private String fwVer;

    private LocalDate dtInstall;
}
