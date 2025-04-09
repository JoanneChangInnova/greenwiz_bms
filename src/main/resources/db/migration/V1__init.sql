CREATE SEQUENCE USER_SEQ
    INCREMENT BY 1
    MINVALUE 1
    MAXVALUE 2147483647
    START 2
    CACHE 1
	NOCYCLE;

CREATE TABLE IF NOT EXISTS `user` (
`id` BIGINT UNSIGNED NOT NULL COMMENT '自增主鍵',
`role` TINYINT UNSIGNED NOT NULL COMMENT '角色代碼: 0:admin, 1:agent, 2:installer, 3:customer',
`parent_id` BIGINT UNSIGNED NULL COMMENT '管理者 ID，對應其他用戶user_id',
`agent_id` BIGINT UNSIGNED NULL COMMENT '代理商 ID，除ADMIN其他用戶必填',
`email` VARCHAR(255) NULL COMMENT '電子郵件地址',
`password` VARCHAR(255) NULL COMMENT '加密密碼',
`username` VARCHAR(128) NULL COMMENT '使用者名稱',
`company` VARCHAR(100) NULL COMMENT '公司名稱',
`contact` VARCHAR(128) NULL COMMENT '聯絡人名稱',
`phone_country_code` VARCHAR(6) NULL COMMENT '電話國碼',
`phone_number` VARCHAR(16) NULL COMMENT '電話號碼',
`country` VARCHAR(3) NULL COMMENT '使用者的國家，ISO 3166-1三位字母代碼',
`level` TINYINT UNSIGNED NULL COMMENT '平臺等級',
`address` VARCHAR(1024) NULL COMMENT '使用者的地址',
`state` TINYINT UNSIGNED NULL COMMENT '用戶狀態: 0:待審, 1:開通, 2:封鎖',
`max_device` SMALLINT UNSIGNED NULL DEFAULT 20 COMMENT '最大設備數量，默認 20',
`language` VARCHAR(10) NULL COMMENT '語言偏好，CHT 或 ENG',
`dt_modify` DATETIME NULL COMMENT '修改時間',
`dt_create` DATETIME NULL COMMENT '創建時間',
`create_user` BIGINT NULL COMMENT '建立資料的使用者 id',
`modify_user` BIGINT NULL COMMENT '修改資料的使用者 id',
PRIMARY KEY (`id`),
UNIQUE KEY `unique_email` (`email`) COMMENT '唯一約束，防止重複的email'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用戶表';

CREATE TABLE IF NOT EXISTS `channel` (
`id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '自增主鍵，唯一標識每個通道',
`factory_id` BIGINT UNSIGNED NULL COMMENT '工廠 ID，關聯工廠資料表的主鍵',
`iot_device_id` BIGINT UNSIGNED NULL COMMENT '關聯iot_device表的主鍵',
`addr` TINYINT UNSIGNED NULL COMMENT 'Modbus 地址，值範圍 1~30，同一個kraken內不可重複',
`name` VARCHAR(50) DEFAULT NULL COMMENT '名稱',
`channel_name` VARCHAR(20) NULL COMMENT '通道代號，格式為四層結構，第一層必為大寫英文字母A~Z，各層用 - 符號連接，二至四層可為01~99數字，例如A-01-99-99，第二層亦可為M，例如A-M，Z-M，同一個工廠內通道代號不可重複。',
`device_type`  VARCHAR(20) NULL COMMENT '設備類型，controller, monitor',
`device_model` VARCHAR(100) NULL COMMENT '設備型號，
Monitor:METSEPM2220,METSEPM1125HCL05RD,IEM2455-230V-100A,SPM80000-60,SPM301-60,SPM1,SW1100-1P3W,SW3200-010,STD640,SMT660
Controller:SWB,CX-IR0001S,AMA-Fans',
`function_mode` VARCHAR(50) NULL COMMENT '功能模式，monitor:1P+N/2P/2P+N/3P/3P+N; controller不填',
`statistics_in_ov` BOOLEAN NULL COMMENT '是否為總用電通道，值為 TRUE 或 FALSE',
`state` TINYINT UNSIGNED NOT NULL COMMENT '狀態，0:offline, 1:online',
`description` VARCHAR(50) DEFAULT NULL COMMENT '描述',
`dt_modify` DATETIME NULL COMMENT '修改時間',
`dt_create` DATETIME NULL COMMENT '創建時間',
`create_user` BIGINT NULL COMMENT '建立資料的使用者 id',
`modify_user` BIGINT NULL COMMENT '修改資料的使用者 id',
PRIMARY KEY (`id`),
UNIQUE KEY `unique_iot_device_addr` (`iot_device_id`, `addr`) COMMENT '唯一約束，防止重複的iot_device_id和addr',
UNIQUE KEY `unique_factory_channel_name` (`factory_id`, `channel_name`) COMMENT '唯一約束，防止重複的factory_id和channel_name'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='通道表，存儲通道相關信息';

CREATE TABLE IF NOT EXISTS `factory` (
 `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '自增主鍵',
 `factory_uuid` UUID NOT NULL DEFAULT UUID() COMMENT '工廠的唯一標識符（UUID）',
 `name` VARCHAR(100) NOT NULL COMMENT '工廠名稱',
 `utc_offset` VARCHAR(6) NOT NULL DEFAULT '+00:00' COMMENT '時區偏移量，格式如 +00:00（範圍 -12:00 到 +14:00）',
 `max_kwh` FLOAT UNSIGNED NOT NULL DEFAULT 100 COMMENT '最大限制功率（kWh）預設100，最大值999999',
 `monitor_period_minute` SMALLINT UNSIGNED NOT NULL DEFAULT 15 COMMENT '監控週期（分鐘)，預設15分鐘',
 `country` VARCHAR(3) NULL COMMENT '國家名稱（ISO 3166-1三位字母代碼）',
 `address` VARCHAR(1024) NULL COMMENT '工廠地址',
 `longitude` DECIMAL(9,6) DEFAULT 0 COMMENT '經度，範圍 -180.000000 到 180.000000',
 `latitude` DECIMAL(9,6) DEFAULT 0 COMMENT '緯度，範圍 -90.000000 到 90.000000',
 `comment` VARCHAR(2048) DEFAULT NULL COMMENT '工廠說明',
 `dt_modify` DATETIME NULL COMMENT '修改時間',
 `dt_create` DATETIME NULL COMMENT '創建時間',
 `create_user` BIGINT NULL COMMENT '建立資料的使用者 id',
 `modify_user` BIGINT NULL COMMENT '修改資料的使用者 id',
 PRIMARY KEY (`id`),
 UNIQUE KEY (`factory_uuid`) COMMENT '唯一約束，防止重複的工廠 UUID'
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='工廠基本信息表';

CREATE TABLE IF NOT EXISTS `iot_device` (
`id` bigint unsigned NOT NULL AUTO_INCREMENT,
`factory_id` BIGINT UNSIGNED NULL COMMENT '工廠 ID，關聯工廠表的主鍵',
`owner_id` BIGINT UNSIGNED NULL COMMENT '用戶 ID，關聯用戶表的主鍵',
`owner_role_id` TINYINT UNSIGNED NULL COMMENT '用戶角色代碼: 0:admin, 1:agent, 2:installer, 3:customer',
`kraken_model` varchar(64) NOT NULL COMMENT '型號別',
`factory_iot_serial` INT unsigned NOT NULL COMMENT '產品序號',
`name` VARCHAR(100) DEFAULT NULL COMMENT '設備名稱',
`state` TINYINT UNSIGNED NOT NULL COMMENT '狀態，0:庫存/ 1:運行中/ 2:斷線',
`fw_ver` char(16) NULL COMMENT '韌體版本',
`dt_install` DATE NULL COMMENT '設備安裝時間',
`dt_modify` DATETIME NULL COMMENT '修改時間',
`dt_create` DATETIME NULL COMMENT '創建時間',
`create_user` BIGINT NULL COMMENT '建立資料的使用者 id',
`modify_user` BIGINT NULL COMMENT '修改資料的使用者 id',
PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS `user_factory`(
 `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '自增主鍵',
 `user_id` BIGINT UNSIGNED NOT NULL COMMENT '用戶 ID，關聯用戶表的主鍵',
 `factory_id` BIGINT UNSIGNED NOT NULL COMMENT '工廠 ID，關聯工廠表的主鍵',
 `dt_modify` DATETIME NULL COMMENT '修改時間',
 `dt_create` DATETIME NULL COMMENT '創建時間',
 `create_user` BIGINT NULL COMMENT '建立資料的使用者 id',
 `modify_user` BIGINT NULL COMMENT '修改資料的使用者 id',
 PRIMARY KEY (`id`),
 UNIQUE KEY `unique_user_factory` (`user_id`, `factory_id`) COMMENT '保證同一用戶和工廠的關係不重複'
)ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用戶與工廠關係表';


-- 第一筆用戶，登入用
INSERT INTO `user`
(id, create_user, dt_create, dt_modify, username, modify_user, address, parent_id, agent_id, company, contact, country, email, `language`, `level`, max_device, password, phone_country_code, phone_number, `role`, state)
VALUES(1, 1, '2025-04-07 01:20:35.810', '2025-04-07 01:20:35.812', 'admin', 1, '', 1, NULL, '', '', 'TWN',
'admin@email.com', 'CHT', NULL, 20, '{bcrypt}$2a$10$bIFhwJ23EwUhX2eflJ0cQuS.mboZ7/0s3KW1Ve/.1PHyOy6uX5rbW', '', '', 0, 1);



-- epower_os_v1.device_model definition

CREATE TABLE `device_model` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(128) NOT NULL COMMENT 'Monitor:METSEPM2220,METSEPM1125HCL05RD,IEM2455-230V-100A,SPM80000-60,SPM301-60,SPM1,SW1100-1P3W,SW3200-010,STD640,SMT660, Controller:SWB,CX-IR0001S,AMA-Fans',
  `channel_type_id` bigint(20) unsigned NOT NULL DEFAULT 0 COMMENT '0: monitor, otherwise: controller',
  `option_setting` longtext DEFAULT NULL COMMENT '{"sets":["set1", "set2", "set3"],"conditions":["10", "15", "20", "25", "30"]}',
  `control_method` longtext DEFAULT NULL COMMENT '{"on":["set1","set2","set3","off"],"auto":{"on":null,"timer":{"set1":"","set2":"","set3":""},"signal":"consumption","off":null},"timer":["set1","set2","set3","off"],"offline":["set1","set2","set3","off","timer"]}',
  `option_cmd` longtext DEFAULT NULL COMMENT '細項資料，monitor: {"detectorType":數值}; controller CX-IR0001S: {"temperature": 16~30}',
  `dt_create` datetime NOT NULL DEFAULT current_timestamp(),
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1004 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='設備型號，\r\nMonitor:METSEPM2220,METSEPM1125HCL05RD,IEM2455-230V-100A,SPM80000-60,SPM301-60,SPM1,SW1100-1P3W,SW3200-010,STD640,SMT660\r\nController:SWB,CX-IR0001S,AMA-Fans';

INSERT INTO device_model
(id, name, channel_type_id, option_setting, control_method, option_cmd, dt_create)
VALUES(1, 'METSEPM2220', 0, NULL, NULL, '{"1P+N":"", "2P":"", "2P+N":"", "3P":"", "3P+N":""}', '2025-03-18 18:58:22.000');
INSERT INTO device_model
(id, name, channel_type_id, option_setting, control_method, option_cmd, dt_create)
VALUES(2, 'METSEPM1125HCL05RD', 0, NULL, NULL, '{"1P+N":"", "2P":"", "2P+N":"", "3P":"", "3P+N":""}', '2025-03-18 18:58:22.000');
INSERT INTO device_model
(id, name, channel_type_id, option_setting, control_method, option_cmd, dt_create)
VALUES(3, 'IEM2455-230V-100A', 0, NULL, NULL, '{"1P+N":"", "2P":"", "2P+N":"", "3P":"", "3P+N":""}', '2025-03-18 18:58:22.000');
INSERT INTO device_model
(id, name, channel_type_id, option_setting, control_method, option_cmd, dt_create)
VALUES(4, 'SPM80000-60', 0, NULL, NULL, '{"1P+N":"", "2P":"", "2P+N":"", "3P":"", "3P+N":""}', '2025-03-18 18:58:22.000');
INSERT INTO device_model
(id, name, channel_type_id, option_setting, control_method, option_cmd, dt_create)
VALUES(5, 'SPM301-60', 0, NULL, NULL, '{"1P+N":"", "2P":"", "2P+N":"", "3P":"", "3P+N":""}', '2025-03-18 18:58:22.000');
INSERT INTO device_model
(id, name, channel_type_id, option_setting, control_method, option_cmd, dt_create)
VALUES(6, 'SPM1', 0, NULL, NULL, '{"1P+N":"", "2P":"", "2P+N":"", "3P":"", "3P+N":""}', '2025-03-18 18:58:22.000');
INSERT INTO device_model
(id, name, channel_type_id, option_setting, control_method, option_cmd, dt_create)
VALUES(7, 'SW1100-1P3W', 0, NULL, NULL, '{"1P+N":"", "2P":"", "2P+N":"", "3P":"", "3P+N":""}', '2025-03-18 18:58:22.000');
INSERT INTO device_model
(id, name, channel_type_id, option_setting, control_method, option_cmd, dt_create)
VALUES(8, 'SW3200-010', 0, NULL, NULL, '{"1P+N":"", "2P":"", "2P+N":"", "3P":"", "3P+N":""}', '2025-03-18 18:58:22.000');
INSERT INTO device_model
(id, name, channel_type_id, option_setting, control_method, option_cmd, dt_create)
VALUES(9, 'STD640', 0, NULL, NULL, '{"1P+N":"", "2P":"", "2P+N":"", "3P":"", "3P+N":""}', '2025-03-18 18:58:22.000');
INSERT INTO device_model
(id, name, channel_type_id, option_setting, control_method, option_cmd, dt_create)
VALUES(10, 'SMT660', 0, NULL, NULL, '{"1P+N":"", "2P":"", "2P+N":"", "3P":"", "3P+N":""}', '2025-03-18 18:58:22.000');
INSERT INTO device_model
(id, name, channel_type_id, option_setting, control_method, option_cmd, dt_create)
VALUES(11, 'SWB', 1, NULL, '{"on":null,"auto":{"timer":null,"signal":"consumption","off":null},"timer":["on","off"],"offline":["on","off","timer"]}', '{"on":[], "off":[]}', '2025-03-18 18:58:22.000');
INSERT INTO device_model
(id, name, channel_type_id, option_setting, control_method, option_cmd, dt_create)
VALUES(12, 'CX-IR0001S', 2, '{"sets":["set1", "set2", "set3"],"conditions":["10", "15", "20", "25", "30"]}', '{"on":["set1","set2","set3","off"],"auto":{"on":null,"timer":{"set1":"","set2":"","set3":""},"signal":"consumption","off":null},"timer":["set1","set2","set3","off"],"offline":["set1","set2","set3","off","timer"]}', '{"on":[], "off":[], "set1":[], "set2":[], "set3":[]}', '2025-03-18 18:58:22.000');
INSERT INTO device_model
(id, name, channel_type_id, option_setting, control_method, option_cmd, dt_create)
VALUES(13, 'AMA-Fans', 3, '{"sets":["set1","set2","set3"],"conditions":{"power":["on","off"],"speed":[0,10,20,30,40,50,60,70,80,90,100],"rotation":["forware","reversal"],"light":["on","off"],"auto":[true,false]}}', '{"on":["set1","set2","set3","off"],"auto":{"on":null,"timer":{"set1":"","set2":"","set3":""},"signal":"consumption","off":null},"timer":["set1","set2","set3","off"],"offline":["set1","set2","set3","off","timer"]}', '{"on":[], "off":[], "set1":[], "set2":[], "set3":[]}', '2025-03-18 18:58:22.000');

-- epower_os_v1.channel_type definition

CREATE TABLE `channel_type` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(50) NOT NULL DEFAULT '',
  `description` varchar(50) NOT NULL DEFAULT '',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='\r\n';

INSERT INTO channel_type
(id, name, description)
VALUES(0, 'monitor', '');
INSERT INTO channel_type
(id, name, description)
VALUES(1, 'Switch', '');
INSERT INTO channel_type
(id, name, description)
VALUES(2, 'IR Control', '');
INSERT INTO channel_type
(id, name, description)
VALUES(3, 'AMA Tech', '');