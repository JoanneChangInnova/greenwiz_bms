package com.greenwiz.bms.facade;

import com.greenwiz.bms.controller.data.channel.AddChannelReq;
import com.greenwiz.bms.entity.Channel;
import com.greenwiz.bms.exception.BmsException;
import com.greenwiz.bms.service.ChannelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ChannelFacade {

    @Autowired
    private ChannelService channelService;


    public Channel addChannel(AddChannelReq req) {

        // 檢查 channelName 是否已存在於該工廠
        if (channelService.existsByFactoryIdAndChannelName(req.getFactoryId(), req.getChannelName())) {
            throw new BmsException("該工廠內已存在相同的通道代號");
        }

        Channel channel = Channel.builder()
                .factoryId(req.getFactoryId())
                .iotDeviceId(req.getIotDeviceId())
                .addr(req.getAddr())
                .name(req.getName())
                .channelName(req.getChannelName())
                .deviceType(req.getDeviceType())
                .deviceModel(req.getDeviceModel())
                .functionType(req.getFunctionType())
                .functionDetail(req.getFunctionDetail())
                .statisticsInOv(req.getStatisticsInOv())
                .state(req.getState())
                .description(req.getDescription())
                .build();

        return channelService.save(channel);
    }
}
