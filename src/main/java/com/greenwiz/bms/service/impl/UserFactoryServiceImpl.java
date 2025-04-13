package com.greenwiz.bms.service.impl;

import com.greenwiz.bms.controller.data.user.UserData;
import com.greenwiz.bms.entity.UserFactory;
import com.greenwiz.bms.repository.UserFactoryRepository;
import com.greenwiz.bms.service.UserFactoryService;
import lombok.AccessLevel;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class UserFactoryServiceImpl extends BaseDomainServiceImpl<Long, UserFactory> implements UserFactoryService {

    @Autowired
    @Getter(value = AccessLevel.PROTECTED)
    private UserFactoryRepository jpaRepository;

    @Override
    public Map<Long, List<Long>> findUserIdsByFactoryIds(List<Long> factoryIds) {
        // 查詢所有關聯記錄
        List<UserFactory> userFactories = jpaRepository.findByFactoryIdIn(factoryIds);
        
        // 將結果轉換為 Map<工廠ID, 用戶ID列表>
        return userFactories.stream()
                .collect(Collectors.groupingBy(
                        UserFactory::getFactoryId,
                        Collectors.mapping(UserFactory::getUserId, Collectors.toList())
                ));
    }

    @Override
    public List<UserData> findUserDataByFactoryId(Long factoryId) {
        return jpaRepository.findUserDataByFactoryId(factoryId);
    }
}
