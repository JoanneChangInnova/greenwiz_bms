package com.greenwiz.bms.service;

import com.greenwiz.bms.dto.ChannelTypeDeviceModelDTO;
import com.greenwiz.bms.dto.DeviceModelSimpleDTO;
import com.greenwiz.bms.repository.ChannelTypeRepository;
import com.greenwiz.bms.repository.DeviceModelRepository;
import com.greenwiz.bms.entity.ChannelType;
import com.greenwiz.bms.entity.DeviceModel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author Johnny 2025/4/8
 */
@Service
@RequiredArgsConstructor
public class ChannelConfigService {
    private final ChannelTypeRepository channelTypeRepository;
    private final DeviceModelRepository deviceModelRepository;


    /**
     * 依照 channelType 的 name 分組，回傳對應的 deviceModel 名稱清單
     */
    public List<ChannelTypeDeviceModelDTO> getChannelTypeWithDeviceModels() {
        // 1. 先用投影把 device_model 的 channelTypeId & name 拉出來
        List<DeviceModelSimpleDTO> deviceList = deviceModelRepository.findAllSimple();
        Map<Long, List<String>> deviceMap = deviceList.stream()
                .collect(Collectors.groupingBy(
                        DeviceModelSimpleDTO::getChannelTypeId,
                        Collectors.mapping(DeviceModelSimpleDTO::getName, Collectors.toList())
                ));

        // 2. 再用投影把 channel_type 的 id & name 拉出來
        return channelTypeRepository.findAllSimple().stream()
                .map(ct -> new ChannelTypeDeviceModelDTO(
                        ct.getId(),
                        ct.getName(),
                        deviceMap.getOrDefault(ct.getId(), Collections.emptyList())
                ))
                .collect(Collectors.toList());
    }

    public List<Map<String, Object>> getAllChannelTypesSimple() {
        return channelTypeRepository.findAllSimple().stream()
                .map(ct -> {
                    Map<String,Object> m = new HashMap<>();
                    m.put("channelTypeId", ct.getId());
                    m.put("name", ct.getName());
                    return m;
                })
                .collect(Collectors.toList());
    }

}
