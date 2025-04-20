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
import java.util.Map;
import java.util.stream.Collectors;

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

    @Override
    public List<KrakenData> findByUserIdsAndFactoryIdIsNull(List<Long> userIds) {
        if (userIds == null || userIds.isEmpty()) {
            return List.of();
        }
        return jpaRepository.findByUserIdInAndFactoryIdIsNull(userIds);
    }

    @Override
    public Map<Long, List<Long>> findKrakenIdsByFactoryIds(List<Long> factoryIds) {
        // 查詢所有關聯的 Kraken
        List<Kraken> krakens = jpaRepository.findByFactoryIdIn(factoryIds);
        
        // 將結果轉換為 Map<工廠ID, Kraken ID列表>
        return krakens.stream()
                .collect(Collectors.groupingBy(
                        Kraken::getFactoryId,
                        Collectors.mapping(Kraken::getId, Collectors.toList())
                ));
    }

    @Override
    public List<Long> findByFactoryId(Long id) {
        return jpaRepository.findByFactoryId(id);
    }

    @Override
    public List<KrakenData> findKrakenDataByFactoryId(Long id) {
        return jpaRepository.findKrakenDataByFactoryId(id);
    }

    @Override
    public Long count() {
        return jpaRepository.count();
    }
}
