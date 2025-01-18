package com.greenwiz.bms.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@Entity
public class Channel {

    /** 自增主鍵，唯一標識每個通道 */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** 工廠 ID，關聯工廠資料表的主鍵 */
    private Long factoryId;

    /** 主機 ID（Gateway ID），關聯主機資料表的主鍵 */
    private Long iotDeviceId;

    /** Modbus 地址，值範圍 1~30，同一個工廠內不可重複 */
    private Integer addr;

    /** 名稱 */
    private String name;

    /** 通道代號，格式為 A-99-99-99 或 A(四層結構)，第一層為 A~Z，第二至第四層為 11~99，同一個工廠內不可重複 */
    private String channelName;

    /** 設備類型，0:controller, 1:monitor */
    private Integer deviceType;

    /** 設備型號 */
    private String deviceModel;

    /** 監測相位類型，0:1P+N, 1:2P, 2:2P+N, 3:3P, 4:3P+N（僅 smartmeter 必填） */
    private Integer breakerType;

    /** 控制類型 */
    private String controlType;

    /** 是否為總用電通道，值為 TRUE 或 FALSE */
    private Boolean statisticsInOv;

    /** 狀態，0:offline, 1:online */
    private Integer state;

    /** 描述信息 */
    private String description;

    /** 最後修改時間 */
    private LocalDateTime dtModify;

    /** 註冊時間 */
    private LocalDateTime dtCreate;

    /** 建立資料的使用者 username 或 SYSTEM */
    private String createUser;

    /** 修改資料的使用者 username 或 SYSTEM */
    private String modifyUser;
}
