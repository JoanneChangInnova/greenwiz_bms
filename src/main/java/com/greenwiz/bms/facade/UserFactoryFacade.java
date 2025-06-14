package com.greenwiz.bms.facade;

import com.greenwiz.bms.entity.Factory;
import com.greenwiz.bms.entity.User;
import com.greenwiz.bms.entity.UserFactory;
import com.greenwiz.bms.enumeration.UserRole;
import com.greenwiz.bms.service.FactoryService;
import com.greenwiz.bms.service.UserFactoryService;
import com.greenwiz.bms.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.Objects;

@Service
public class UserFactoryFacade {

    @Autowired
    private UserFactoryService userFactoryService;

    @Autowired
    private UserService userService;

    @Autowired
    private FactoryService factoryService;

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
            // TODO: 低能需求要移除
            cleanupUserIdOwner(userId, toRemove);
            userFactoryService.deleteAll(toRemove);
        }

        // Add new bindings
        if (!toAdd.isEmpty()) {
            userFactoryService.saveAllAndFlush(toAdd);
        }
        // TODO: 低能需求要移除
        syncUserIdOwnerToFactory(userId, factoryIds);
    }

    public Set<Long> getFactoryIdsByUserId(Long userId) {
        return userFactoryService.findByUserId(userId).stream()
                .map(UserFactory::getFactoryId)
                .collect(Collectors.toSet());
    }


    /*
     * TODO: 低能需求要移除
     * */
    private void syncUserIdOwnerToFactory(Long userId, Set<Long> factoryIds) {
        if (factoryIds == null || factoryIds.isEmpty())
            return;

        User user = userService.findByPk(userId);
        if (user == null || user.getRole() != UserRole.CUSTOMER)
            return;

        for (Long factoryId : factoryIds) {
            Factory factory = factoryService.findByPk(factoryId);
            if (factory != null && factory.getUserIdOwner() == null) {
                factory.setUserIdOwner(userId);
                factoryService.save(factory);
            }
        }
    }

    /*
     * TODO: 低能需求要移除
     * */
    private void cleanupUserIdOwner(Long userId, List<UserFactory> toRemove) {
        User user = userService.findByPk(userId);
        if (user == null || user.getRole() != UserRole.CUSTOMER) return;

        for (UserFactory uf : toRemove) {
            Factory factory = factoryService.findByPk(uf.getFactoryId());
            if (factory != null && Objects.equals(factory.getUserIdOwner(), userId)) {
                factory.setUserIdOwner(null); // 移除 owner 資訊
                factoryService.save(factory);
            }
        }
    }
}
