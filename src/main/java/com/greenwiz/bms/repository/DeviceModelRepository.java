package com.greenwiz.bms.repository;

/**
 * @author Johnny 2025/4/8
 */
import com.greenwiz.bms.dto.DeviceModelSimpleDTO;
import com.greenwiz.bms.entity.DeviceModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DeviceModelRepository extends JpaRepository<DeviceModel, Long> {

    @Query("SELECT new com.greenwiz.bms.dto.DeviceModelSimpleDTO(dm.channelTypeId, dm.name) FROM DeviceModel dm")
    List<DeviceModelSimpleDTO> findAllSimple();


}
