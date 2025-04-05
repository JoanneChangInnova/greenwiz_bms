package com.greenwiz.bms.facade;

import com.greenwiz.bms.controller.data.factory.AddFactoryReq;
import com.greenwiz.bms.controller.data.factory.ListFactoryReq;
import com.greenwiz.bms.controller.data.factory.UpdateFactoryReq;
import com.greenwiz.bms.entity.Channel;
import com.greenwiz.bms.entity.Factory;
import com.greenwiz.bms.entity.Kraken;
import com.greenwiz.bms.enumeration.Country;
import com.greenwiz.bms.enumeration.UserRole;
import com.greenwiz.bms.exception.BmsException;
import com.greenwiz.bms.service.ChannelService;
import com.greenwiz.bms.service.FactoryService;
import com.greenwiz.bms.service.KrakenService;
import com.greenwiz.bms.service.UserFactoryService;
import com.greenwiz.bms.utils.ThreadLocalUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class FactoryFacade {

    @Autowired
    private KrakenService krakenService;

    @Autowired
    private ChannelService channelService;

    @Autowired
    private FactoryService factoryService;

    @Autowired
    private UserFactoryService userFactoryService;

    /**
     * 新增工廠並綁定 Kraken 設備
     */
    public void addFactory(AddFactoryReq addFactoryReq) {
        // 驗證並獲取 Kraken 列表
        List<Kraken> krakenList = validateAndGetKrakenList(addFactoryReq.getIotDeviceIds());
        
        List<Channel> channelList = List.of();
        if (!CollectionUtils.isEmpty(krakenList)) {
            // 檢查 Kraken 是否已綁定工廠
            validateKrakenBindings(krakenList);
            channelList = validateAndGetChannelList(krakenList);
            validateChannelBindings(krakenList, channelList);
            // 檢查 channel name 唯一性
            validateChannelNameUniqueness(channelList);
        }
        
        // 保存工廠信息
        Factory savedFactory = saveFactory(addFactoryReq);
        
        // 只有當有設備需要綁定時才更新綁定關係
        if (!CollectionUtils.isEmpty(krakenList)) {
            updateDeviceBindings(savedFactory, krakenList, channelList);
        }
    }

    /**
     * 檢查 Kraken 綁定狀態：
     * Kraken 綁定工廠前，factory_id 必須為空，若非空需要使用者去工廠編輯中，將該工廠綁定的kraken移除後，才能綁定新工廠
     */
    private void validateKrakenBindings(List<Kraken> krakenList) {
        List<Kraken> alreadyBoundKrakens = krakenList.stream()
                .filter(kraken -> kraken.getFactoryId() != null)
                .toList();
        
        if (!alreadyBoundKrakens.isEmpty()) {
            String boundKrakens = alreadyBoundKrakens.stream()
                    .map(kraken -> String.format("kraken Id: %d", kraken.getId()))
                    .collect(Collectors.joining(", "));
            throw new BmsException(String.format("%s 已綁定工廠，請先至原工廠移除綁定後再試", boundKrakens));
        }
    }

    /**
     * 照理說不應出錯的檢查
     * 檢查 Channel 綁定狀態：
     * 每個 Kraken 底下的 channel 的 factory_id 必須為空才能綁定，
     * 當 Channel 有選擇 kraken 則會自動帶入 kraken 的 factory_id，
     * 故 validateKrakenBindings 應能檢測出錯誤
     * 若 Kraken 沒綁定工廠 channel 卻有綁定，則為系統錯誤，需由管理員修改資料
     */
    private void validateChannelBindings(List<Kraken> krakenList, List<Channel> channelList) {

        // 檢查 Channel 的工廠綁定狀態
        for (Kraken kraken : krakenList) {
            List<Channel> krakenChannels = channelList.stream()
                    .filter(channel -> channel.getIotDeviceId().equals(kraken.getId()))
                    .filter(channel -> channel.getFactoryId() != null)
                    .toList();
            
            if (!krakenChannels.isEmpty()) {
                String boundChannels = krakenChannels.stream()
                        .map(channel -> String.format("channel Id: %d", channel.getId()))
                        .collect(Collectors.joining(", "));
                throw new BmsException(String.format("kraken Id: %d 的 %s 已綁定工廠，請聯繫管理員",
                        kraken.getId(), boundChannels));
            }
        }
    }

    /**
     * 檢查 Channel 名稱唯一性
     */
    private void validateChannelNameUniqueness(List<Channel> channelList) {
        Map<String, List<Channel>> channelNameGroups = channelList.stream()
                .collect(Collectors.groupingBy(Channel::getChannelName));
        
        List<String> duplicateMessages = channelNameGroups.entrySet().stream()
                .filter(entry -> entry.getValue().size() > 1)
                .map(entry -> {
                    String channelName = entry.getKey();
                    String krakenIds = entry.getValue().stream()
                            .map(channel -> String.format("kraken Id: %d", channel.getIotDeviceId()))
                            .collect(Collectors.joining(", "));
                    return String.format("通道代號: %s (%s)", channelName, krakenIds);
                })
                .toList();
        
        if (!duplicateMessages.isEmpty()) {
            throw new BmsException(String.format("發現重複的通道代號：\n%s",
                    String.join("\n", duplicateMessages)));
        }
    }

    /**
     * 驗證並獲取 Kraken 列表
     */
    private List<Kraken> validateAndGetKrakenList(List<Long> iotDeviceIds) {
        if (CollectionUtils.isEmpty(iotDeviceIds)) {
            return List.of();
        }
        
        List<Kraken> krakenList = krakenService.findByIdIn(iotDeviceIds);
        if (krakenList.size() != iotDeviceIds.size()) {
            Set<Long> foundIds = krakenList.stream()
                    .map(Kraken::getId)
                    .collect(Collectors.toSet());
            List<Long> notFoundIds = iotDeviceIds.stream()
                    .filter(id -> !foundIds.contains(id))
                    .toList();
            throw new BmsException("Kraken ID 不存在: " + notFoundIds);
        }
        return krakenList;
    }

    /**
     * 獲取並驗證 Channel 列表
     */
    private List<Channel> validateAndGetChannelList(List<Kraken> krakenList) {
        List<Long> krakenIds = krakenList.stream()
                .map(Kraken::getId)
                .collect(Collectors.toList());
        return channelService.findByIotDeviceIdIn(krakenIds);
    }

    /**
     * 檢查 Channel 綁定狀態
     */
    private void validateChannelBindings(List<Channel> channelList) {
        List<Channel> alreadyBondFactoryChannelList = channelList.stream()
                .filter(channel -> channel.getFactoryId() != null)
                .toList();
        
        if (!alreadyBondFactoryChannelList.isEmpty()) {
            String boundChannels = alreadyBondFactoryChannelList.stream()
                    .map(channel -> String.format("Channel[id=%d, name=%s]", channel.getId(), channel.getChannelName()))
                    .collect(Collectors.joining(", "));
            throw new BmsException("以下 Channel 已綁定工廠: " + boundChannels);
        }
    }

    /**
     * 保存工廠信息
     */
    private Factory saveFactory(AddFactoryReq addFactoryReq) {
        Factory factory = new Factory();
        BeanUtils.copyProperties(addFactoryReq, factory);
        Country country = addFactoryReq.getCountry();
        if (country != null) {
            factory.setCountry(country.name());
        }
        factory.setFactoryUuid(UUID.randomUUID());
        return factoryService.saveAndFlush(factory);
    }

    /**
     * 更新設備綁定關係
     * 任一筆更新失敗，則Factory應一起Rollback
     */
    private void updateDeviceBindings(Factory factory, List<Kraken> krakenList, List<Channel> channelList) {
        // 更新 Kraken 綁定關係
        krakenList.forEach(kraken -> {
            kraken.setFactoryId(factory.getId());
            krakenService.save(kraken);
        });

        // 更新 Channel 綁定關係
        channelList.forEach(channel -> {
            channel.setFactoryId(factory.getId());
            channelService.save(channel);
        });
    }


    public void updateFactory(Long id, UpdateFactoryReq request) {

    }

    public Page<Factory> getFactoryList(ListFactoryReq listFactoryReq, UserRole role) {
        /**
         * ADMIN可以看所有的factory list
         * AGENT只能看user_factory中user_id為自己群組的factory:
         * user_id為自己 = user.parent_id = 登入者id
         * AGENT: 先找出 user.parent_id = 登入者id 代表找出所有安裝商的user，例如id 2,3，再繼續找出user.parent_id = 2, 3 的客戶
         * 這些客戶的 id 才去 user_factory 找資料
         *
         */
        ExampleMatcher matcher = ExampleMatcher.matching().withIgnoreNullValues()
                .withMatcher("name", ExampleMatcher.GenericPropertyMatchers.contains());
        Factory factory = new Factory();
        factory.setId(listFactoryReq.getFactoryId());
        factory.setName(listFactoryReq.getName());
        Example<Factory> example = Example.of(factory, matcher);

        if(role == UserRole.ADMIN){
            return factoryService.getFactoryPageBySpecification(example, listFactoryReq.getPageable());
        }

        return null;
    }
}
