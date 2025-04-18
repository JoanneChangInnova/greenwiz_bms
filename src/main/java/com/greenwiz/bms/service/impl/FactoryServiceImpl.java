package com.greenwiz.bms.service.impl;

import com.greenwiz.bms.controller.data.factory.FactoryBasicData;
import com.greenwiz.bms.entity.Factory;
import com.greenwiz.bms.repository.FactoryRepository;
import com.greenwiz.bms.service.FactoryService;
import lombok.AccessLevel;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public class FactoryServiceImpl extends BaseDomainServiceImpl<Long, Factory> implements FactoryService {

    @Autowired
    @Getter(value = AccessLevel.PROTECTED)
    private FactoryRepository jpaRepository;

    @Override
    public Page<Factory> getFactoryPageBySpecification(Specification<Factory> spec, Pageable pageable) {
        return jpaRepository.findAll(spec, pageable);
    }

    @Override
    public List<Factory> findByAgentId(Long agentId) {
        return jpaRepository.findByAgentId(agentId);
    }

    @Override
    public Set<FactoryBasicData> findFactoryBasicDataByFactoryIds(Set<Long> factoryIds) {
        return jpaRepository.findFactoryBasicDataByFactoryIds(factoryIds);
    }
}
