package com.greenwiz.bms.controller.data.kraken;

import com.greenwiz.bms.entity.Kraken;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class KrakenData {
    private Long iotDeviceId;
    private String name;

    /**
     * 將 Kraken 實體轉換為 KrakenData
     */
    public static KrakenData convertToKrakenData(Kraken kraken) {
        if (kraken == null) {
            return null;
        }
        
        return KrakenData.builder()
                .iotDeviceId(kraken.getId())
                .name(kraken.getName())
                .build();
    }
}
