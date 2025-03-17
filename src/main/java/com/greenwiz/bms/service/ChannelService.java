package com.greenwiz.bms.service;

import com.greenwiz.bms.entity.Channel;

import java.util.List;

public interface ChannelService extends BaseDomainService<Long, Channel> {

    boolean existsByFactoryIdAndChannelName(Long factoryId, String channelName);

    List<Channel> findByIotDeviceIdOrderByAddrAsc(Long iotDeviceId);
}
