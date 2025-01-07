package com.greenwiz.bms.controller.data.kraken;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AddKrakenReq {
    private Long factoryId;
    private Long userId;
    private String krakenModel;
    private Integer factoryKrakenSerial;
    private String name;
    private Integer state;
    private String fwVer;
    private LocalDateTime dtInstall;
}
