package com.greenwiz.bms.controller.data.kraken;

import com.fasterxml.jackson.annotation.JsonSetter;
import com.greenwiz.bms.controller.data.base.PageReq;
import com.greenwiz.bms.enumeration.KrakenState;
import com.greenwiz.bms.enumeration.UserRole;
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
    private KrakenState state;
    private UserRole userRole;
    private String fwVer;
    private LocalDate dtInstall;
    private String createUser;
    private String modifyUser;
}
