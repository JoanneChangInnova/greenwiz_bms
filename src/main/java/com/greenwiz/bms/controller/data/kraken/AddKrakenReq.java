package com.greenwiz.bms.controller.data.kraken;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
public class AddKrakenReq {
    private Long factoryId;

    private Long userId;

    @NotBlank(message = "型號別不能為空")
    private String krakenModel;

    @NotNull(message = "產品序號不能為空")
    private Integer factoryIotSerial;

    private String name;

    @NotNull(message = "狀態不能為空")
    private Integer state;

    private String fwVer;

    private LocalDate dtInstall;
}
