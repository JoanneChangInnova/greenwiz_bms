package com.greenwiz.bms.service;

import com.greenwiz.bms.entity.Factory;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
public interface FactoryService extends BaseDomainService<Long, Factory> {

    Page<Factory> getFactoryPageBySpecification(Specification<Factory> spec, Pageable pageable);
}
