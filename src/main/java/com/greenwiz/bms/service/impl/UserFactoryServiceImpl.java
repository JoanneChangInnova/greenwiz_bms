package com.greenwiz.bms.service.impl;

import com.greenwiz.bms.entity.UserFactory;
import com.greenwiz.bms.repository.UserFactoryRepository;
import com.greenwiz.bms.service.UserFactoryService;
import lombok.AccessLevel;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserFactoryServiceImpl extends BaseDomainServiceImpl<Long, UserFactory> implements UserFactoryService {
    @Autowired
    @Getter(value = AccessLevel.PROTECTED)
    private UserFactoryRepository jpaRepository;
}
