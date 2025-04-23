package com.greenwiz.bms.dto;

import lombok.AllArgsConstructor;
import lombok.Data;


/**
 * @author Johnny 2025/4/24
 */
@Data
@AllArgsConstructor
public class DeviceModelSimpleDTO {
    private Long channelTypeId;
    private String name;
}