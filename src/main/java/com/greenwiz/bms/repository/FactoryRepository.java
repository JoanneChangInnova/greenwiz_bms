package com.greenwiz.bms.repository;

import com.greenwiz.bms.controller.data.factory.FactoryBasicData;
import com.greenwiz.bms.entity.Factory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface FactoryRepository extends JpaRepository<Factory,Long> , JpaSpecificationExecutor<Factory> {

    List<Factory> findByAgentId(Long agentId);

    @Query("SELECT new com.greenwiz.bms.controller.data.factory.FactoryBasicData(f.id, f.name, f.factoryUuid) " +
            "FROM Factory f WHERE f.id IN :factoryIds")
    Set<FactoryBasicData> findFactoryBasicDataByFactoryIds(Set<Long> factoryIds);
}
