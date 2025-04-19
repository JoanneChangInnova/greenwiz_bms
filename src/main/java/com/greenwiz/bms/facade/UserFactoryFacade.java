package com.greenwiz.bms.facade;

import com.greenwiz.bms.entity.UserFactory;
import com.greenwiz.bms.service.UserFactoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserFactoryFacade {

    @Autowired
    private UserFactoryService userFactoryService;

    public void updateUserFactoryBindings(Long userId, Set<Long> factoryIds) {
        List<UserFactory> userFactories = userFactoryService.findByUserId(userId);

        //去除已綁定過的factoryId
        List<Long> existFactoryIds = new ArrayList<>();
        for(UserFactory userFactory : userFactories){
            existFactoryIds.add(userFactory.getFactoryId());
        }
        existFactoryIds.forEach(factoryIds::remove);

        //新增未綁定的user與工廠
        List<UserFactory> userFactoryList = new ArrayList<>();
        for(Long factoryId : factoryIds){
            UserFactory userFactory = new UserFactory();
            userFactory.setFactoryId(factoryId);
            userFactory.setUserId(userId);
            userFactoryList.add(userFactory);
        }
        userFactoryService.saveAllAndFlush(userFactoryList);
    }

    public Set<Long> getFactoryIdsByUserId(Long userId) {
        return userFactoryService.findByUserId(userId).stream()
                .map(UserFactory::getFactoryId)
                .collect(Collectors.toSet());
    }
}
