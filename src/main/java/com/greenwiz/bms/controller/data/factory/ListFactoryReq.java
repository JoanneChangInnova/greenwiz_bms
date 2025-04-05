package com.greenwiz.bms.controller.data.factory;

import com.greenwiz.bms.controller.data.base.PageReq;
import lombok.Data;

@Data
public class ListFactoryReq extends PageReq {

    /**
     * 工廠名稱
     */
    private String name;

    private Long userId;

    private Long krakenId;

    private Long factoryId;
}
