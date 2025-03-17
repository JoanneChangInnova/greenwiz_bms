package com.greenwiz.bms.service.impl;

import com.greenwiz.bms.entity.Channel;
import com.greenwiz.bms.repository.ChannelRepository;
import com.greenwiz.bms.service.ChannelService;
import lombok.AccessLevel;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ChannelServiceImpl extends BaseDomainServiceImpl<Long, Channel> implements ChannelService {

    @Autowired
    @Getter(value = AccessLevel.PROTECTED)
    private ChannelRepository jpaRepository;

    @Override
    public boolean existsByFactoryIdAndChannelName(Long factoryId, String channelName) {
        return jpaRepository.existsByFactoryIdAndChannelName(factoryId, channelName);
    }

    @Override
    public List<Channel> findByIotDeviceIdOrderByAddrAsc(Long iotDeviceId) {
        return jpaRepository.findByIotDeviceIdOrderByAddrAsc(iotDeviceId);
    }
}
