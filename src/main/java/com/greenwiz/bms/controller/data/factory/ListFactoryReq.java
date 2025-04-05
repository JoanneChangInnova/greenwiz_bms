package com.greenwiz.bms.controller.data.factory;

import com.greenwiz.bms.controller.data.base.PageReq;
import lombok.Data;

@Data
public class ListFactoryReq extends PageReq {

    private String name;        // 對應 Factory.name
    private Long userId;        // 對應 UserFactory.userId
    private Long krakenId;      // 對應 IotDevice.id
    private Long factoryId;     // 對應 Factory.id
}
