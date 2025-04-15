package com.greenwiz.bms.repository;

import com.greenwiz.bms.entity.Factory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface FactoryRepository extends JpaRepository<Factory,Long> , JpaSpecificationExecutor<Factory> {

}
