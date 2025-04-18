package com.greenwiz.bms.service;

import com.greenwiz.bms.controller.data.user.UserData;
import com.greenwiz.bms.entity.UserFactory;

import java.util.List;
import java.util.Map;

public interface UserFactoryService extends BaseDomainService<Long, UserFactory> {
    /**
     * 根據工廠ID列表查詢對應的用戶ID列表
     * @param factoryIds 工廠ID列表
     * @return Map<工廠ID, 用戶ID列表>
     */
    Map<Long, List<Long>> findUserIdsByFactoryIds(List<Long> factoryIds);

    List<UserData> findUserDataByFactoryId(Long id);

    List<UserFactory> findByUserId(Long userId);
}
