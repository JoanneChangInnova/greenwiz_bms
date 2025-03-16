package com.greenwiz.bms.facade;

import com.greenwiz.bms.controller.data.channel.AddChannelReq;
import com.greenwiz.bms.entity.Channel;
import com.greenwiz.bms.entity.Kraken;
import com.greenwiz.bms.exception.BmsException;
import com.greenwiz.bms.service.ChannelService;
import com.greenwiz.bms.service.KrakenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ChannelFacade {

    @Autowired
    private ChannelService channelService;

    @Autowired
    private KrakenService krakenService;


    public Channel addChannel(AddChannelReq req) {
        setFactoryIdToChannel(req);
        validateChannelNameDuplicateInFactory(req.getFactoryId(), req.getChannelName());

        Channel channel =
                Channel.builder().factoryId(req.getFactoryId()).iotDeviceId(req.getIotDeviceId()).addr(req.getAddr())
                        .name(req.getName()).channelName(req.getChannelName()).deviceType(req.getDeviceType())
                        .deviceModel(req.getDeviceModel()).functionMode(req.getFunctionMode())
                        .statisticsInOv(req.getStatisticsInOv()).state(req.getState()).description(req.getDescription())
                        .build();

        return channelService.save(channel);
    }

    /**
     * 如果有選iot_device_id，且kraken已綁定factory，則自動帶入所屬的factory_id
     * 新增/編輯 Channel 檢驗
     */
    private void setFactoryIdToChannel(AddChannelReq req) {
        if (req.getIotDeviceId() != null) {
            Kraken kraken = krakenService.findByPk(req.getIotDeviceId());
            if (kraken == null) {
                throw new BmsException("kraken設備ID 不存在");
            }
            if (kraken.getFactoryId() != null) {
                req.setFactoryId(kraken.getFactoryId());
            }
        }
    }

    /**
     * 如果有factory_id，則檢驗是否有重複的factory_id和channel_name
     * 新增檢驗
     * 編輯時檢驗需跳過自己（只有factory_id變動時才需檢驗/或exists的channel不是自己)
     */
    private void validateChannelNameDuplicateInFactory(Long factoryId, String channelName) {
        if (factoryId == null) {
            return;
        }
        if (channelService.existsByFactoryIdAndChannelName(factoryId, channelName)) {
            throw new BmsException("工廠ID " + factoryId + " 已存在相同的通道代號");
        }
    }
}
