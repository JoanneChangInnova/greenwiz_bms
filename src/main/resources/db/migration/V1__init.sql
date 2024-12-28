CREATE TABLE IF NOT EXISTS `user` (
    `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '自增主鍵，作為唯一標識',
    `role` TINYINT UNSIGNED NOT NULL COMMENT '角色代碼: 0:admin, 1:agent, 2:installer, 3:customer',
    `parent_id` BIGINT UNSIGNED NULL COMMENT '父帳號 ID，對應其他用戶的 ID',
    `email` VARCHAR(255) NULL COMMENT '電子郵件地址',
    `password` VARCHAR(255) NULL COMMENT '加密密碼',
    `username` VARCHAR(128) NULL COMMENT '使用者名稱',
    `company` VARCHAR(100) NULL COMMENT '公司名稱',
    `contact` VARCHAR(128) NULL COMMENT '聯絡人名稱',
    `phone_country_code` VARCHAR(6) NULL COMMENT '電話的國家區號',
    `phone_number` VARCHAR(16) NULL COMMENT '電話號碼',
    `country` VARCHAR(64) NULL COMMENT '使用者的國家',
    `level` TINYINT UNSIGNED NULL COMMENT '平臺等級',
    `address` VARCHAR(1024) NULL COMMENT '使用者的地址',
    `state` TINYINT UNSIGNED NULL COMMENT '用戶狀態: 0:待審, 1:開通, 2:封鎖',
    `max_device` SMALLINT UNSIGNED NULL DEFAULT 20 COMMENT '最大設備數量，默認 20',
    `language` VARCHAR(20) NULL COMMENT '語言偏好，例如 CHT 或 ENG',
    `dt_modify` DATETIME NULL COMMENT '最後修改時間',
    `dt_create` DATETIME NULL COMMENT '註冊時間',
    `create_user` VARCHAR(20) NULL COMMENT '建立資料的使用者 ID 或 SYSTEM',
    `modify_user` VARCHAR(20) NULL COMMENT '修改資料的使用者 ID 或 SYSTEM',
    PRIMARY KEY (`id`)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用戶表';

CREATE TABLE IF NOT EXISTS `channel` (
    `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '自增主鍵，唯一標識每個通道',
    `factory_id` BIGINT UNSIGNED NULL COMMENT '工廠 ID，關聯工廠資料表的主鍵',
    `iot_device_id` BIGINT UNSIGNED NULL COMMENT '主機 ID（Gateway ID），關聯主機資料表的主鍵',
    `addr` TINYINT UNSIGNED NULL COMMENT 'Modbus 地址，值範圍 1~30，同一個工廠內不可重複',
    `name` VARCHAR(50) DEFAULT NULL COMMENT '名稱',
    `channel_name` VARCHAR(50) NULL COMMENT '通道代號，格式為 A-99-99-99 或 A(四層結構)，第一層為 A~Z，第二至第四層為 11~99，同一個工廠內不可重複',
    `device_type` TINYINT UNSIGNED NULL COMMENT '設備類型，0:controller, 1:monitor',
    `device_model` VARCHAR(50) NULL COMMENT '設備型號',
    `breaker_type` TINYINT UNSIGNED NULL COMMENT '監測相位類型，0:1P+N, 1:2P, 2:2P+N, 3:3P, 4:3P+N（僅 smartmeter 必填）',
    `control_type` VARCHAR(50) NULL COMMENT '控制類型',
    `statistics_in_ov` BOOLEAN NULL COMMENT '是否為總用電通道，值為 TRUE 或 FALSE',
    `state` TINYINT UNSIGNED NOT NULL COMMENT '狀態，0:offline, 1:online',
    `description` VARCHAR(50) DEFAULT NULL COMMENT '描述信息   n',
    `dt_modify` DATETIME NULL COMMENT '最後修改時間',
    `dt_create` DATETIME NULL COMMENT '註冊時間',
    `create_user` VARCHAR(20) NULL COMMENT '建立資料的使用者 ID 或 SYSTEM',
    `modify_user` VARCHAR(20) NULL COMMENT '修改資料的使用者 ID 或 SYSTEM',
    PRIMARY KEY (`id`)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='通道表，存儲通道相關信息';

-- ALTER TABLE column_update_time
--     ADD CONSTRAINT unique_ticket_oid_column_update_time_setting_oid UNIQUE (ticket_oid, column_update_time_setting_oid);
