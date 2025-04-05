package com.greenwiz.bms.controller.data.factory;

import com.greenwiz.bms.entity.Factory;
import com.greenwiz.bms.enumeration.Country;
import lombok.Data;
import lombok.experimental.Delegate;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
public class ListFactoryData {
    private Long id;
    private UUID factoryUuid;
    private String name;
    private String utcOffset;
    private Float maxKwh;
    private Short monitorPeriodMinute;
    private Country country;
    private String address;
    private BigDecimal longitude;
    private BigDecimal latitude;
    private String comment;
    private LocalDateTime dtModify;
    private LocalDateTime dtCreate;
    private Long createUser;
    private Long modifyUser;
    private List<Long> iotDeviceIds;  // 關聯的 IoT 設備 ID 列表
    private List<Long> userIds;       // 關聯的用戶 ID 列表
}
