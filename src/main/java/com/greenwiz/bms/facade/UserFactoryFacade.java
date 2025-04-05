package com.greenwiz.bms.facade;

import com.greenwiz.bms.entity.UserFactory;
import com.greenwiz.bms.service.UserFactoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class UserFactoryFacade {

    @Autowired
    private UserFactoryService userFactoryService;

    public void updateUserFactoryBindings(Long factoryId, Set<Long> userIds) {
        List<UserFactory> userFactoryList = new ArrayList<>();
        for(Long userId : userIds){
            UserFactory userFactory = new UserFactory();
            userFactory.setFactoryId(factoryId);
            userFactory.setUserId(userId);
            userFactoryList.add(userFactory);
        }
        userFactoryService.saveAllAndFlush(userFactoryList);
    }
}
