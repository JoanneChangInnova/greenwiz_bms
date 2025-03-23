package com.greenwiz.bms.service;

import com.greenwiz.bms.entity.Channel;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.List;

public interface ChannelService extends BaseDomainService<Long, Channel> {

    boolean existsByFactoryIdAndChannelName(Long factoryId, String channelName);

    List<Channel> findByIotDeviceIdOrderByAddrAsc(Long iotDeviceId);

    Page<Channel> findAll(Example<Channel> example, PageRequest pageable);
}
