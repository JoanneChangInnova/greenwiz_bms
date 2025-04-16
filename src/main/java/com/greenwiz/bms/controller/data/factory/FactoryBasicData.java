package com.greenwiz.bms.controller.data.factory;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class FactoryBasicData {
    private Long factoryId;
    private String name;
    private java.util.UUID UUID;

    public String getDisplayText() {
        return name + " (" + UUID + ")";
    }
}
