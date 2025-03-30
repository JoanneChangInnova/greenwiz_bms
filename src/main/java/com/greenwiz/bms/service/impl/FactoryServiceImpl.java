package com.greenwiz.bms.service.impl;

import com.greenwiz.bms.entity.Factory;
import com.greenwiz.bms.repository.FactoryRepository;
import com.greenwiz.bms.service.FactoryService;
import lombok.AccessLevel;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FactoryServiceImpl extends BaseDomainServiceImpl<Long, Factory> implements FactoryService {

    @Autowired
    @Getter(value = AccessLevel.PROTECTED)
    private FactoryRepository jpaRepository;
}
