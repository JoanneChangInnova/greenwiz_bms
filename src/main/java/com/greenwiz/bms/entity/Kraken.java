package com.greenwiz.bms.entity;

import com.greenwiz.bms.enumeration.KrakenState;
import com.greenwiz.bms.enumeration.UserRole;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Table(name = "iot_device")
public class Kraken extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "factory_id")
    private Long factoryId;

    @Column(name = "owner_id")
    private Long userId;

    @Column(name = "owner_role_id")
    private UserRole userRole;

    @Column(name = "kraken_model", nullable = false, length = 64)
    private String krakenModel;

    @Column(name = "factory_iot_serial", nullable = false)
    private Integer factoryIotSerial;

    @Column(length = 100)
    private String name;

    @Enumerated(EnumType.ORDINAL)
    @Column(nullable = false)
    private KrakenState state;

    @Column(name = "fw_ver", length = 16)
    private String fwVer;

    @Column(name = "dt_install")
    private LocalDate dtInstall;
}