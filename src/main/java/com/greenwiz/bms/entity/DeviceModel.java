package com.greenwiz.bms.entity;

import jakarta.persistence.*;
import lombok.*;

/**
 * @author Johnny 2025/4/7
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@Entity
public class DeviceModel extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Column(name = "channel_type_id")
    private Long channelTypeId;

    @Column(columnDefinition = "LONGTEXT")
    private String optionSetting;

    @Column(columnDefinition = "LONGTEXT")
    private String controlMethod;

    @Column(columnDefinition = "LONGTEXT")
    private String optionCmd;
}