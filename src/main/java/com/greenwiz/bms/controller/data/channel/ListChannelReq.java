package com.greenwiz.bms.controller.data.channel;

import com.greenwiz.bms.controller.data.base.PageReq;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class ListChannelReq extends PageReq {

    /**
     * 工廠 ID，關聯工廠資料表的主鍵。
     */
    private Long factoryId;

    /**
     * 關聯 iot_device 表的主鍵。
     */
    private Long iotDeviceId;

}
