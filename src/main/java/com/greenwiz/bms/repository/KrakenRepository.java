package com.greenwiz.bms.repository;

import com.greenwiz.bms.controller.data.kraken.KrakenData;
import com.greenwiz.bms.entity.Kraken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface KrakenRepository extends JpaRepository<Kraken, Long> {
    Kraken getByName(String name);

    @Query("SELECT new com.greenwiz.bms.controller.data.kraken.KrakenData(k.id, k.name) FROM Kraken k")
    List<KrakenData> findAllKrakenData();
}
