package com.greenwiz.bms.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Table(name = "iot_device")
public class Kraken extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long factoryId;
    private Long userId;
    private String krakenModel;
    private Integer factoryIotSerial;
    private String name;
    private Integer state;
    private String fwVer;
    private LocalDate dtInstall;
}