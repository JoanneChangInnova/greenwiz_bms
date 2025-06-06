package com.greenwiz.bms.service.impl;

import com.greenwiz.bms.entity.Channel;
import com.greenwiz.bms.repository.ChannelRepository;
import com.greenwiz.bms.service.ChannelService;
import lombok.AccessLevel;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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

    @Override
    public Page<Channel> findAll(Example<Channel> example, PageRequest pageable) {
        return jpaRepository.findAll(example, pageable);
    }

    @Override
    public List<Channel> findByIotDeviceIdIn(List<Long> krakenIds) {
        return jpaRepository.findByIotDeviceIdIn(krakenIds);
    }

    @Override
    public Long count() {
        return jpaRepository.count();
    }
}
