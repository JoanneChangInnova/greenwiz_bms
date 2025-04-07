package com.greenwiz.bms.repository;

/**
 * @author Johnny 2025/4/8
 */
import com.greenwiz.bms.entity.DeviceModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DeviceModelRepository extends JpaRepository<DeviceModel, Long> {
}
