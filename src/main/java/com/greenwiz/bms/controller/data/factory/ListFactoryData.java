package com.greenwiz.bms.controller.data.factory;

import com.greenwiz.bms.controller.data.user.CreateModifyUser;
import com.greenwiz.bms.controller.data.user.UserData;
import com.greenwiz.bms.entity.Factory;
import com.greenwiz.bms.enumeration.Country;
import com.greenwiz.bms.service.KrakenService;
import com.greenwiz.bms.service.UserFactoryService;
import com.greenwiz.bms.service.UserService;
import lombok.Data;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Data
public class ListFactoryData {
    private Long id;
    private UUID factoryUuid;
    private String name;
    private String utcOffset;
    private Float maxKwh;
    private Short monitorPeriodMinute;
    private Country country;
    private String address;
    private BigDecimal longitude;
    private BigDecimal latitude;
    private String comment;
    private LocalDateTime dtModify;
    private LocalDateTime dtCreate;
    private Long createUser;
    private Long modifyUser;
    private List<Long> iotDeviceIds;  // 關聯的 IoT 設備 ID 列表
    private List<Long> customerIds;       // 關聯的用戶 ID 列表
    private Long agentId;
    /**
     * agent userName + (email)
     */
    private String agentDisplayText;
    private CreateModifyUser createModifyUser;

    /**
     * 將 Factory 實體列表轉換為 ListFactoryData 列表
     * @param factories Factory 實體列表
     * @param userService 用戶服務
     * @param krakenService Kraken 服務
     * @param userFactoryService 用戶工廠關聯服務
     * @return ListFactoryData 列表
     */
    public static Page<ListFactoryData> convertToListFactoryData(
            Page<Factory> factories,
            UserService userService,
            KrakenService krakenService,
            UserFactoryService userFactoryService
    ) {
        if (factories == null || factories.isEmpty()) {
            return Page.empty();
        }

        List<Factory> factoryList = factories.getContent();

        // 1. 收集所有需要的用戶ID (createUser, modifyUser, agentId)
        Set<Long> userIds = new HashSet<>();
        factoryList.forEach(factory -> {
            userIds.add(factory.getCreateUser());
            userIds.add(factory.getModifyUser());
            userIds.add(factory.getAgentId());
        });
        userIds.remove(null); // 移除可能的 null 值

        // 2. 批量查詢用戶資料
        List<UserData> users = userService.findUserDataListByIds(new ArrayList<>(userIds));
        Map<Long, UserData> userMap = users.stream()
                .collect(Collectors.toMap(UserData::getId, Function.identity()));

        // 3. 收集所有工廠ID並查詢關聯的 Kraken
        List<Long> factoryIds = factoryList.stream()
                .map(Factory::getId)
                .collect(Collectors.toList());
        Map<Long, List<Long>> factoryKrakenMap = krakenService.findKrakenIdsByFactoryIds(factoryIds);

        // 4. 查詢工廠關聯的用戶
        Map<Long, List<Long>> factoryUserMap = userFactoryService.findUserIdsByFactoryIds(factoryIds);

        // 5. 轉換資料
        List<ListFactoryData> resultList = factoryList.stream()
                .map(factory -> {
                    ListFactoryData data = new ListFactoryData();
                    // 複製基本屬性
                    data.setId(factory.getId());
                    data.setFactoryUuid(factory.getFactoryUuid());
                    data.setName(factory.getName());
                    data.setUtcOffset(factory.getUtcOffset());
                    data.setMaxKwh(factory.getMaxKwh());
                    data.setMonitorPeriodMinute(factory.getMonitorPeriodMinute());
                    data.setCountry(factory.getCountry());
                    data.setAddress(factory.getAddress());
                    data.setLongitude(factory.getLongitude());
                    data.setLatitude(factory.getLatitude());
                    data.setComment(factory.getComment());
                    data.setDtModify(factory.getDtModify());
                    data.setDtCreate(factory.getDtCreate());
                    data.setCreateUser(factory.getCreateUser());
                    data.setModifyUser(factory.getModifyUser());
                    data.setAgentId(factory.getAgentId());
                    data.setAgentDisplayText(userMap.get(factory.getAgentId()).getDisplayText());


                    // 設置關聯的 Kraken IDs
                    data.setIotDeviceIds(factoryKrakenMap.getOrDefault(factory.getId(), Collections.emptyList()));

                    // 設置關聯的用戶 IDs
                    data.setCustomerIds(factoryUserMap.getOrDefault(factory.getId(), Collections.emptyList()));

                    // 設置代理商顯示文字
                    UserData agent = userMap.get(factory.getAgentId());
                    if (agent != null) {
                        data.setAgentDisplayText(agent.getDisplayText());
                    }

                    // 設置創建/修改用戶資訊
                    CreateModifyUser createModifyUser = new CreateModifyUser();
                    UserData createUserData = userMap.get(factory.getCreateUser());
                    UserData modifyUserData = userMap.get(factory.getModifyUser());
                    if (createUserData != null) {
                        createModifyUser.setCreateUserInfo(createUserData.getDisplayText());
                    }
                    if (modifyUserData != null) {
                        createModifyUser.setModifyUserInfo(modifyUserData.getDisplayText());
                    }
                    data.setCreateModifyUser(createModifyUser);

                    return data;
                })
                .collect(Collectors.toList());

        return new PageImpl<>(resultList, factories.getPageable(), factories.getTotalElements());
    }
}
