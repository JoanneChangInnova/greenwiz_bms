package com.greenwiz.bms.repository;

import com.greenwiz.bms.entity.Factory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FactoryRepository extends JpaRepository<Factory,Long> , JpaSpecificationExecutor<Factory> {

    List<Factory> findByAgentId(Long agentId);
}
