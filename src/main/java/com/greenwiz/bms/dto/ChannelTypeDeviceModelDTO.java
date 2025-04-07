package com.greenwiz.bms.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

/**
 * @author Johnny 2025/4/8
 */
@Data
@AllArgsConstructor
public class ChannelTypeDeviceModelDTO {
    private Long channelTypeId;
    private String name;
    private List<String> deviceModels;
}
