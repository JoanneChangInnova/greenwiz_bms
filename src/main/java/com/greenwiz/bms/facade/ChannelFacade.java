package com.greenwiz.bms.facade;

import com.greenwiz.bms.controller.data.channel.AddChannelReq;
import com.greenwiz.bms.controller.data.channel.ListChannelReq;
import com.greenwiz.bms.controller.data.channel.UpdateChannelReq;
import com.greenwiz.bms.entity.Channel;
import com.greenwiz.bms.entity.Kraken;
import com.greenwiz.bms.exception.BmsException;
import com.greenwiz.bms.service.ChannelService;
import com.greenwiz.bms.service.KrakenService;
import com.greenwiz.bms.utils.ValidationUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ChannelFacade {

    @Autowired
    private ChannelService channelService;

    @Autowired
    private KrakenService krakenService;


    public void addChannel(AddChannelReq req) {
        Channel channel = new Channel();
        BeanUtils.copyProperties(req, channel);
        setFactoryIdToChannel(channel, req.getIotDeviceId());
        validateChannelNameDuplicateInFactory(channel.getFactoryId(), req.getChannelName());
        setAddr(channel, req.getIotDeviceId());
        channelService.save(channel);
    }

    /**
     * 讀取kraken底下有哪些channel，Addr從中找出最小者填入，範圍1~30，若有跳號1,2,4 則新增要填3，
     * 同一kraken底下不可以重複。
     * 如果update channel時更換kraken ID 則 addr 也會一起變換，原本佔據的addr會空出來
     */
    private void setAddr(Channel channel, Long iotDeviceId) {
        if (iotDeviceId == null) {
            throw new BmsException("kraken ID 不能為空");
        }

        List<Channel> channels = channelService.findByIotDeviceIdOrderByAddrAsc(iotDeviceId);

        // 提取已使用的 addr 值
        Set<Integer> usedAddrs = channels.stream().map(Channel::getAddr).filter(Objects::nonNull) // 避免 null 值
                .collect(Collectors.toSet());

        // 遍歷 1~30，找出第一個未被使用的 addr 並設定給 channel
        for (int i = 1; i <= 30; i++) {
            if (!usedAddrs.contains(i)) {
                channel.setAddr(i);
                return;
            }
        }

        // 若 1~30 均已被佔用，則拋出異常
        throw new BmsException("kraken ID: " + iotDeviceId + "Addr(Modbus) 1~30 已全數佔用，無法再新增Channel");
    }


    /**
     * 如果有選iot_device_id，且kraken已綁定factory，則自動帶入所屬的factory_id
     * 新增/編輯 Channel 檢驗
     */
    private void setFactoryIdToChannel(Channel channel, Long iotDeviceId) {
        if (iotDeviceId == null) {
            throw new BmsException("kraken ID 不能為空");
        }

        Kraken kraken = krakenService.findByPk(iotDeviceId);
        if (kraken == null) {
            throw new BmsException("kraken ID 不存在");
        }

        if (kraken.getFactoryId() != null) {
            channel.setFactoryId(kraken.getFactoryId());
        }
    }

    /**
     * 如果有factory_id，則檢驗是否有重複的factory_id和channel_name
     * 新增檢驗
     * 編輯時檢驗需跳過自己（只有factory_id變動時才需檢驗/或exists的channel不是自己)
     */
    public void validateChannelNameDuplicateInFactory(Long factoryId, String channelName) {
        if (factoryId == null) {
            return;
        }
        if (channelService.existsByFactoryIdAndChannelName(factoryId, channelName)) {
            throw new BmsException(
                    "工廠ID " + factoryId + " 已存在相同的通道代號，請確認kraken ID 和 通道代號 是否設置正確");
        }
    }

    public Page<Channel> getChannelList(ListChannelReq listChannelReq) {
        ExampleMatcher matcher = ExampleMatcher.matching().withIgnoreNullValues();
        Channel channel = new Channel();
        BeanUtils.copyProperties(listChannelReq, channel);
        Example<Channel> example = Example.of(channel, matcher);
        return channelService.findAll(example, listChannelReq.getPageable());
    }

    public void updateChannel(Long id, UpdateChannelReq request) {
        Channel channel = channelService.findByPk(id);
        if (channel == null) {
            throw new BmsException("Channel不存在");
        }
        ValidationUtils.validateVersion(request.getDtModify(),channel.getDtModify());
        // 如果Kraken ID變更 factory_id 和 addr 都要變更
        if (!request.getIotDeviceId().equals(channel.getIotDeviceId())) {
            setFactoryIdToChannel(channel, request.getIotDeviceId());
            setAddr(channel, request.getIotDeviceId());
        }
        if (!Objects.equals(channel.getFactoryId(), request.getFactoryId())) {
            validateChannelNameDuplicateInFactory(channel.getFactoryId(), request.getChannelName());
        }
        channel.setIotDeviceId(request.getIotDeviceId());
        channel.setName(request.getName());
        channel.setChannelName(request.getChannelName());
        channel.setDeviceType(request.getDeviceType());
        channel.setDeviceModel(request.getDeviceModel());
        channel.setFunctionMode(request.getFunctionMode());
        channel.setStatisticsInOv(request.getStatisticsInOv());
        channel.setState(request.getState());
        channel.setDescription(request.getDescription());

        channelService.save(channel);
    }
}
