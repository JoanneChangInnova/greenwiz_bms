package com.greenwiz.bms.facade;

import com.greenwiz.bms.controller.data.factory.*;
import com.greenwiz.bms.controller.data.kraken.KrakenData;
import com.greenwiz.bms.controller.data.user.UserData;
import com.greenwiz.bms.entity.*;
import com.greenwiz.bms.enumeration.Country;
import com.greenwiz.bms.enumeration.UserRole;
import com.greenwiz.bms.exception.BmsException;
import com.greenwiz.bms.service.*;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Subquery;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;
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
    private UserFactoryFacade userFactoryFacade;

    @Autowired
    private UserService userService;

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

//        if (!CollectionUtils.isEmpty(addFactoryReq.getUserIds())) {
//            userFactoryFacade.updateUserFactoryBindings(savedFactory.getId(), addFactoryReq.getUserIds());
//        }
    }

    /**
     * 新增Factory時
     * 檢查 Kraken 綁定狀態：
     * Kraken 綁定工廠前，factory_id 必須為空，若非空需要使用者去工廠編輯中，將該工廠綁定的kraken移除後，才能綁定新工廠
     */
    private void validateKrakenBindings(List<Kraken> krakenList) {
        List<Kraken> alreadyBoundKrakens = krakenList.stream().filter(kraken -> kraken.getFactoryId() != null).toList();

        if (!alreadyBoundKrakens.isEmpty()) {
            String boundKrakens =
                    alreadyBoundKrakens.stream().map(kraken -> String.format("kraken Id: %d", kraken.getId()))
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
            List<Channel> krakenChannels =
                    channelList.stream().filter(channel -> channel.getIotDeviceId().equals(kraken.getId()))
                            .filter(channel -> channel.getFactoryId() != null).toList();

            if (!krakenChannels.isEmpty()) {
                String boundChannels =
                        krakenChannels.stream().map(channel -> String.format("channel Id: %d", channel.getId()))
                                .collect(Collectors.joining(", "));
                throw new BmsException(
                        String.format("kraken Id: %d 的 %s 已綁定工廠，請聯繫管理員", kraken.getId(), boundChannels));
            }
        }
    }

    /**
     * 檢查 Channel 名稱唯一性
     */
    private void validateChannelNameUniqueness(List<Channel> channelList) {
        Map<String, List<Channel>> channelNameGroups =
                channelList.stream().collect(Collectors.groupingBy(Channel::getChannelName));

        List<String> duplicateMessages =
                channelNameGroups.entrySet().stream().filter(entry -> entry.getValue().size() > 1).map(entry -> {
                    String channelName = entry.getKey();
                    String krakenIds = entry.getValue().stream()
                            .map(channel -> String.format("kraken Id: %d", channel.getIotDeviceId()))
                            .collect(Collectors.joining(", "));
                    return String.format("通道代號: %s (%s)", channelName, krakenIds);
                }).toList();

        if (!duplicateMessages.isEmpty()) {
            throw new BmsException(String.format("發現重複的通道代號：\n%s", String.join("\n", duplicateMessages)));
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
            Set<Long> foundIds = krakenList.stream().map(Kraken::getId).collect(Collectors.toSet());
            List<Long> notFoundIds = iotDeviceIds.stream().filter(id -> !foundIds.contains(id)).toList();
            throw new BmsException("Kraken ID 不存在: " + notFoundIds);
        }
        return krakenList;
    }

    /**
     * 獲取並驗證 Channel 列表
     */
    private List<Channel> validateAndGetChannelList(List<Kraken> krakenList) {
        List<Long> krakenIds = krakenList.stream().map(Kraken::getId).collect(Collectors.toList());
        return channelService.findByIotDeviceIdIn(krakenIds);
    }

    /**
     * 保存工廠信息
     */
    private Factory saveFactory(AddFactoryReq addFactoryReq) {
        Factory factory = new Factory();
        BeanUtils.copyProperties(addFactoryReq, factory);
        Country country = addFactoryReq.getCountry();
        if (country != null) {
            factory.setCountry(country);
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

    public FactoryData getFactory(Long id) {
        Factory factory = factoryService.findByPk(id);
        if (factory == null) {
            throw new BmsException("工廠不存在");
        }
        List<KrakenData> factoryKrakens = krakenService.findKrakenDataByFactoryId(id);

        List<UserData> factoryCustomers = userFactoryService.findUserDataByFactoryId(id);

        UserData agentUser = userService.getUserDataByUserId(factory.getAgentId());
        String agentDisplayText = agentUser != null ? agentUser.getDisplayText() : null;

        return FactoryData.builder().factory(factory).factoryKrakens(factoryKrakens).factoryCustomers(factoryCustomers)
                .agentDisplayText(agentDisplayText).build();
    }

    public void updateFactory(Long id, UpdateFactoryReq request) {
        Factory factory = factoryService.findByPk(id);
        if (factory == null) {
            throw new BmsException("工廠不存在");
        }

        List<KrakenData> factoryKrakens = krakenService.findKrakenDataByFactoryId(id);
        Set<Long> oldKrakenIds = factoryKrakens.stream()
                .map(KrakenData::getIotDeviceId)
                .collect(Collectors.toSet());

        List<Long> requestIotDeviceIds = request.getIotDeviceIds() != null ? request.getIotDeviceIds() : List.of();

        // 找出要刪除的 Kraken ID（存在於 oldKrakenIds 但不在 requestIotDeviceIds 中）
        List<Long> krakenIdsToRemove = oldKrakenIds.stream()
                .filter(iotDeviceId -> !requestIotDeviceIds.contains(iotDeviceId))
                .toList();

        // 找出要新增的 Kraken ID（存在於 requestIotDeviceIds 但不在 oldKrakenIds 中）
        List<Long> newKrakenIds = requestIotDeviceIds.stream()
                .filter(iotDeviceId -> !oldKrakenIds.contains(iotDeviceId))
                .toList();

        // 處理要刪除的 Kraken，將其 factory_id 設為 null，並更新其 channel 的 factory_id
        if (!krakenIdsToRemove.isEmpty()) {
            List<Kraken> krakensToRemove = krakenService.findByIdIn(krakenIdsToRemove);
            List<Channel> channelsToRemove = channelService.findByIotDeviceIdIn(krakenIdsToRemove);
            //Map<krakenId, channelList>
            Map<Long, List<Channel>> channelMap = channelsToRemove.stream()
                    .collect(Collectors.groupingBy(Channel::getIotDeviceId));

            for (Kraken kraken : krakensToRemove) {
                kraken.setFactoryId(null);
                // 更新該 Kraken 底下的 channel
                List<Channel> channels = channelMap.get(kraken.getId());
                if (channels != null) {
                    channels.forEach(channel -> channel.setFactoryId(null));
                    channelService.saveAll(channels);
                }
            }
            krakenService.saveAll(krakensToRemove);
        }

        List<Kraken> newKrakenList = List.of();
        List<Channel> channelList = List.of();

        // 處理新增的 Kraken ID
        if (!newKrakenIds.isEmpty()) {
            newKrakenList = validateAndGetKrakenList(newKrakenIds);
            // 檢查新加入的 Kraken 是否未綁定工廠
            validateKrakenBindings(newKrakenList);
            // 獲取並檢查新 Kraken 的 channel 是否未綁定
            channelList = channelService.findByIotDeviceIdIn(newKrakenIds);
            validateChannelBindings(newKrakenList, channelList);
            // 檢查通道名稱唯一性
            validateChannelNameUniqueness(channelList);
        }

        // 更新工廠信息
        BeanUtils.copyProperties(request, factory, "country"); // 排除 country 以單獨處理
        Country country = request.getCountry();
        if (country != null) {
            factory.setCountry(country);
        }
        factoryService.save(factory);

        // 更新設備綁定（僅針對新增的 Kraken）
        if (!newKrakenList.isEmpty()) {
            updateDeviceBindings(factory, newKrakenList, channelList);
        }
    }

    /**
     * 如果 UserRole 是
     * ADMIM :
     * 可以看所有的factory list，直接select all factory list
     * AGENT :
     * 只能看到部分工廠列表，需帶入factory_id in:
     * 搜尋 agent_id = ThreadLocalUtils.getUser().getId() And role = 3 的 UserIds
     * 再到 UserFactory 找符合這些user的 工廠ID list
     */
    public Page<Factory> getFactoryList(ListFactoryReq listFactoryReq, UserRole role) {
        Specification<Factory> spec = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (listFactoryReq.getName() != null) {
                predicates.add(cb.like(root.get("name"), "%" + listFactoryReq.getName() + "%"));
            }

            if (listFactoryReq.getFactoryId() != null) {
                predicates.add(cb.equal(root.get("id"), listFactoryReq.getFactoryId()));
            }

            // 查詢 userId（子查詢）
            if (listFactoryReq.getUserId() != null) {
                Subquery<Long> userFactorySubquery = query.subquery(Long.class);
                Root<UserFactory> userFactoryRoot = userFactorySubquery.from(UserFactory.class);
                userFactorySubquery.select(userFactoryRoot.get("factoryId"))
                        .where(cb.equal(userFactoryRoot.get("userId"), listFactoryReq.getUserId()));
                predicates.add(root.get("id").in(userFactorySubquery));
            }

            // 查詢 krakenId（子查詢）
            if (listFactoryReq.getKrakenId() != null) {
                Subquery<Long> iotDeviceSubquery = query.subquery(Long.class);
                Root<Kraken> iotDeviceRoot = iotDeviceSubquery.from(Kraken.class);
                iotDeviceSubquery.select(iotDeviceRoot.get("factoryId"))
                        .where(cb.equal(iotDeviceRoot.get("id"), listFactoryReq.getKrakenId()));
                predicates.add(root.get("id").in(iotDeviceSubquery));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
        /**
         * SELECT DISTINCT f.*
         * FROM Factory f
         * LEFT JOIN UserFactory uf ON f.id = uf.factory_id
         * LEFT JOIN IotDevice idv ON f.id = idv.factory_id
         * WHERE 1=1
         *   AND (f.name LIKE CONCAT('%', :name, '%') OR :name IS NULL)
         *   AND (f.id = :factoryId OR :factoryId IS NULL)
         *   AND (uf.user_id = :userId OR :userId IS NULL)
         *   AND (idv.id = :krakenId OR :krakenId IS NULL)
         * ORDER BY f.id
         * LIMIT :pageSize OFFSET :offset;
         */
        if (role == UserRole.ADMIN) {
            return factoryService.getFactoryPageBySpecification(spec, listFactoryReq.getPageable());
        }
        return Page.empty();
    }

    public Set<FactoryBasicData> findByInstallerId(Long installerId) {
        //find user by installerId, check if user role is installer
        User installer = userService.findByPk(installerId);
        if (installer.getRole() != UserRole.INSTALLER) {
            throw new BmsException("用戶管理者非安裝商");
        }
        List<Factory> factories = factoryService.findByAgentId(installer.getAgentId());
        // 將工廠轉換為 FactoryBasicData
        return factories.stream()
                .map(factory -> FactoryBasicData.builder().factoryId(factory.getId()).name(factory.getName()).UUID(factory.getFactoryUuid()).build())
                .collect(Collectors.toSet());
    }
}
