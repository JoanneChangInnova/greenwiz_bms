package com.greenwiz.bms.service.impl;

import com.greenwiz.bms.controller.data.kraken.KrakenData;
import com.greenwiz.bms.entity.Kraken;
import com.greenwiz.bms.repository.KrakenRepository;
import com.greenwiz.bms.service.KrakenService;
import lombok.AccessLevel;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class KrakenServiceImpl extends BaseDomainServiceImpl<Long, Kraken> implements KrakenService {

    @Autowired
    @Getter(value = AccessLevel.PROTECTED)
    private KrakenRepository jpaRepository;

    @Override
    public Page<Kraken> getKrakenPageBySpecification(Example<Kraken> example, Pageable pageable) {
        return jpaRepository.findAll(example, pageable);
    }

    @Override
    public Kraken getByName(String name) {
        return jpaRepository.getByName(name);
    }

    @Override
    public List<KrakenData> findAllKrakenData() {
        return jpaRepository.findAllKrakenData();
    }

    @Override
    public List<Kraken> findByIdIn(List<Long> iotDeviceIds) {
        return jpaRepository.findByIdIn(iotDeviceIds);
    }

    @Override
    public List<Kraken> findByFactoryIdIsNull() {
        return jpaRepository.findByFactoryIdIsNull();
    }

    @Override
    public List<Kraken> findByUserId(Long id) {
        return jpaRepository.findByUserId(id);
    }
}
