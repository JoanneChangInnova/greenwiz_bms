package com.greenwiz.bms.controller;

import com.greenwiz.bms.dto.ChannelTypeDeviceModelDTO;
import com.greenwiz.bms.service.ChannelConfigService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * @author Johnny 2025/4/8
 */
@RestController
@RequestMapping("/api/V1/channel-config")
@RequiredArgsConstructor
public class ChannelConfigController {

    private final ChannelConfigService channelConfigService;

    /**
     * 取得依 channelType 分組的 deviceModel.name 對應清單
     */
    @GetMapping("/device-models/grouped")
    public List<ChannelTypeDeviceModelDTO> getChannelTypeWithModels() {
        return channelConfigService.getChannelTypeWithDeviceModels();
    }

}
