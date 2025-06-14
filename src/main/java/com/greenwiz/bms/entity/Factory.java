package com.greenwiz.bms.entity;

import com.greenwiz.bms.enumeration.Country;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Table(name = "factory")
public class Factory extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "factory_uuid", nullable = false, unique = true)
    private UUID factoryUuid;

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Column(name = "utc_offset", nullable = false, length = 6)
    private String utcOffset;

    @Column(name = "max_kwh", nullable = false)
    private Float maxKwh;

    @Column(name = "monitor_period_minute", nullable = false)
    private Short monitorPeriodMinute;

    @Enumerated(EnumType.STRING)
    @Column(name = "country", length = 3)
    private Country country;

    @Column(name = "address", length = 1024)
    private String address;

    @Column(name = "longitude", precision = 9, scale = 6)
    private BigDecimal longitude;

    @Column(name = "latitude", precision = 9, scale = 6)
    private BigDecimal latitude;

    @Column(name = "comment", length = 2048)
    private String comment;

    @Column(name = "agent_id", nullable = false)
    private Long agentId;

    /*
    * TODO: 低能需求要移除
    * */
    @Column(name = "user_id_owner")
    private Long userIdOwner;

}
