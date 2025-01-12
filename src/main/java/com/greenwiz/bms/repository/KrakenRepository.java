package com.greenwiz.bms.repository;

import com.greenwiz.bms.entity.Kraken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface KrakenRepository extends JpaRepository<Kraken, Long> {
    Kraken getByName(String name);
}
