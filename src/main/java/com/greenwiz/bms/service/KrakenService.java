package com.greenwiz.bms.service;

import com.greenwiz.bms.controller.data.kraken.KrakenData;
import com.greenwiz.bms.entity.Kraken;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface KrakenService extends BaseDomainService<Long, Kraken> {
    Page<Kraken> getKrakenPageBySpecification(Example<Kraken> example, Pageable pageable);

    Kraken getByName(String name);

    List<KrakenData> findAllKrakenData();
}