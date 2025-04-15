package com.greenwiz.bms.controller.data.factory;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FactoryBasicData {
    private Long factoryId;
    private String name;
    private String UUID;

    public String getDisplayText() {
        return name + " (" + UUID + ")";
    }
}
