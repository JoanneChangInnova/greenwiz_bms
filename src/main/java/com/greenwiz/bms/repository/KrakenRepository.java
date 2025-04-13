package com.greenwiz.bms.repository;

import com.greenwiz.bms.controller.data.kraken.KrakenData;
import com.greenwiz.bms.entity.Kraken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface KrakenRepository extends JpaRepository<Kraken, Long> {
    Kraken getByName(String name);

    @Query("SELECT new com.greenwiz.bms.controller.data.kraken.KrakenData(k.id, k.name) FROM Kraken k")
    List<KrakenData> findAllKrakenData();

    List<Kraken> findByIdIn(List<Long> iotDeviceIds);
    List<Kraken> findByFactoryIdIsNull();

    List<Kraken> findByUserId(Long id);

    @Query("SELECT new com.greenwiz.bms.controller.data.kraken.KrakenData(k.id, k.name) FROM Kraken k WHERE k.userId IN :userIds AND k.factoryId IS NULL")
    List<KrakenData> findByUserIdInAndFactoryIdIsNull(@Param("userIds") List<Long> userIds);

    /**
     * 根據工廠ID列表查詢關聯的Kraken列表
     */
    List<Kraken> findByFactoryIdIn(List<Long> factoryIds);

    List<Long> findByFactoryId(Long id);

    @Query("SELECT new com.greenwiz.bms.controller.data.kraken.KrakenData(k.id, k.name) FROM Kraken k WHERE k.factoryId = :id")
    List<KrakenData> findKrakenDataByFactoryId(Long id);
}
