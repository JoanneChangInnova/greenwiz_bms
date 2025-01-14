package com.greenwiz.bms.controller.data.kraken;

import com.greenwiz.bms.controller.data.base.PageReq;
import lombok.Data;

import java.time.LocalDate;

@Data
public class ListKrakenReq extends PageReq {
    private Long id;
    private Long factoryId;
    private Long userId;
    private String krakenModel;
    private Integer factoryIotSerial;
    private String name;
    private Integer state;
    private String fwVer;
    private LocalDate dtInstall;
    private String createUser;
    private String modifyUser;
}
