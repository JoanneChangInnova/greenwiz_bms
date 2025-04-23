package com.greenwiz.bms.repository;

import com.greenwiz.bms.dto.ChannelTypeSimpleDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import com.greenwiz.bms.entity.ChannelType;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
 * @author Johnny 2025/4/8
 */
@Repository
public interface ChannelTypeRepository extends JpaRepository<ChannelType, Long> {

    @Query("SELECT new com.greenwiz.bms.dto.ChannelTypeSimpleDTO(ct.id, ct.name) FROM ChannelType ct")
    List<ChannelTypeSimpleDTO> findAllSimple();

}