package com.greenwiz.bms.repository;

import com.greenwiz.bms.entity.Channel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChannelRepository extends JpaRepository<Channel, Long> {
    boolean existsByFactoryIdAndChannelName(Long factoryId, String channelName);

    List<Channel> findByIotDeviceIdOrderByAddrAsc(Long iotDeviceId);
}
