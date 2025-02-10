package com.greenwiz.bms.entity;

import com.greenwiz.bms.enumeration.DeviceType;
import com.greenwiz.bms.enumeration.State;
import jakarta.persistence.*;
import lombok.*;

/**
 * 通道實體類，對應資料表 `channel`，存儲通道相關信息。
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@Entity
public class Channel extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 工廠 ID，關聯工廠資料表的主鍵。
     */
    private Long factoryId;

    /**
     * 關聯 iot_device 表的主鍵。
     */
    private Long iotDeviceId;

    /**
     * Modbus 地址，值範圍 1~30，同一個工廠內不可重複。
     */
    private Integer addr;

    /**
     * 名稱。
     */
    private String name;

    /**
     * 通道代號，格式為 A-99-99-99 或 A(四層結構)，
     * 第一層為 A~Z，第二至第四層為 11~99，同一個工廠內不可重複。
     */
    private String channelName;

    /**
     * 設備類型，例如：controller 或 monitor。
     */
    @Enumerated(EnumType.STRING)
    private DeviceType deviceType;

    /**
     * 設備型號，例如：
     * Monitor: METSEPM2220, METSEPM1125HCL05RD, IEM2455-230V-100A 等。
     * Controller: SWB, CX-IR0001S, AMA-Fans 等。
     */
    private String deviceModel;

    /**
     * 功能模式，例如：
     * - monitor: 1P+N/2P/2P+N/3P/3P+N
     * - SWB: ON/OFF
     * - AMA-Fans: 3/2/1/OFF
     * - CX-IR0001S: Auto/Only Air/Strong Air/OFF
     */
    private String functionType;

    /**
     * 細項資料 (JSON 格式)。
     * - monitor: {"detectorType":數值}
     * - controller CX-IR0001S: {"temperature": 16~30}
     */
    private String functionDetail;

    /**
     * 是否為總用電通道，值為 TRUE 或 FALSE。
     */
    private Boolean statisticsInOv;

    /**
     * 狀態，0 表示 offline，1 表示 online。
     */
    @Enumerated
    private State state;

    /**
     * 描述。
     */
    private String description;

}
