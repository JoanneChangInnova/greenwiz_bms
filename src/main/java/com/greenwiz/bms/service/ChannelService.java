package com.greenwiz.bms.service;

import com.greenwiz.bms.entity.Channel;

public interface ChannelService extends BaseDomainService<Long, Channel> {

    boolean existsByFactoryIdAndChannelName(Long factoryId, String channelName);

}
