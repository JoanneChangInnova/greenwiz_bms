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

        // Create a set of current factory IDs for easier comparison
        Set<Long> currentFactoryIds = userFactories.stream()
                .map(UserFactory::getFactoryId)
                .collect(Collectors.toSet());

        // Find factories to add (in factoryIds but not in currentFactoryIds)
        List<UserFactory> toAdd = factoryIds.stream()
                .filter(factoryId -> !currentFactoryIds.contains(factoryId))
                .map(factoryId -> {
                    UserFactory uf = new UserFactory();
                    uf.setUserId(userId);
                    uf.setFactoryId(factoryId);
                    return uf;
                })
                .collect(Collectors.toList());

        // Find factories to remove (in currentFactoryIds but not in factoryIds)
        List<UserFactory> toRemove = userFactories.stream()
                .filter(uf -> !factoryIds.contains(uf.getFactoryId()))
                .collect(Collectors.toList());

        // Remove obsolete bindings
        if (!toRemove.isEmpty()) {
            userFactoryService.deleteAll(toRemove);
        }

        // Add new bindings
        if (!toAdd.isEmpty()) {
            userFactoryService.saveAllAndFlush(toAdd);
        }
    }

    public Set<Long> getFactoryIdsByUserId(Long userId) {
        return userFactoryService.findByUserId(userId).stream()
                .map(UserFactory::getFactoryId)
                .collect(Collectors.toSet());
    }
}
