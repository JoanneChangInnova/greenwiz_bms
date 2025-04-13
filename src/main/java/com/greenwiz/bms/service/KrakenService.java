package com.greenwiz.bms.service;

import com.greenwiz.bms.controller.data.kraken.KrakenData;
import com.greenwiz.bms.entity.Kraken;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public interface KrakenService extends BaseDomainService<Long, Kraken> {
    Page<Kraken> getKrakenPageBySpecification(Example<Kraken> example, Pageable pageable);

    Kraken getByName(String name);

    List<KrakenData> findAllKrakenData();

    List<Kraken> findByIdIn(List<Long> iotDeviceIds);

    /**
     * 查詢未綁定工廠的 Kraken 列表
     */
    List<Kraken> findByFactoryIdIsNull();

    List<Kraken> findByUserId(Long id);

    /**
     * 查詢指定用戶列表中未綁定工廠的 Kraken
     * @param userIds 用戶ID列表
     * @return 未綁定工廠的 Kraken 資料列表
     */
    List<KrakenData> findByUserIdsAndFactoryIdIsNull(List<Long> userIds);

    /**
     * 根據工廠ID列表查詢對應的Kraken ID列表
     * @param factoryIds 工廠ID列表
     * @return Map<工廠ID, Kraken ID列表>
     */
    Map<Long, List<Long>> findKrakenIdsByFactoryIds(List<Long> factoryIds);

    List<Long> findByFactoryId(Long id);

    List<KrakenData> findKrakenDataByFactoryId(Long id);
}