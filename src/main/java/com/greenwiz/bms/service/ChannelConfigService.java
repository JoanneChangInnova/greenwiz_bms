package com.greenwiz.bms.service;

import com.greenwiz.bms.dto.ChannelTypeDeviceModelDTO;
import com.greenwiz.bms.repository.ChannelTypeRepository;
import com.greenwiz.bms.repository.DeviceModelRepository;
import com.greenwiz.bms.entity.ChannelType;
import com.greenwiz.bms.entity.DeviceModel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
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
        Map<Long, List<String>> deviceMap = deviceModelRepository.findAll().stream()
                .collect(Collectors.groupingBy(
                        DeviceModel::getChannelTypeId,
                        Collectors.mapping(DeviceModel::getName, Collectors.toList())
                ));

        return channelTypeRepository.findAll().stream()
                .map(ct -> new ChannelTypeDeviceModelDTO(
                        ct.getId(),         // 現在對應的是 channelTypeId
                        ct.getName(),
                        deviceMap.getOrDefault(ct.getId(), Collections.emptyList())
                ))
                .collect(Collectors.toList());
    }
}
