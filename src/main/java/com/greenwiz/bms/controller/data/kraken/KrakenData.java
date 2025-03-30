package com.greenwiz.bms.controller.data.kraken;

import lombok.Data;

@Data
public class KrakenData {

    private Long iot_device_id;

    private String name;

    public KrakenData(Long id, String name) {
        this.iot_device_id = id;  // 將資料庫的 id 賦值給 iot_device_id
        this.name = name;
    }
}
