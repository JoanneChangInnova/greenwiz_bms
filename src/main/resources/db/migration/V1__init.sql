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
    `state` TINYINT UNSIGNED NULL COMMENT '帳戶狀態: 0:待審, 1:開通, 2:封鎖',
    `max_device` SMALLINT UNSIGNED NULL DEFAULT 20 COMMENT '最大設備數量，默認 20',
    `language` VARCHAR(20) NULL COMMENT '語言偏好，例如 CHT 或 ENG',
    `dt_modify` DATETIME NULL COMMENT '最後修改時間',
    `dt_create` DATETIME NULL COMMENT '註冊時間',
    `create_user` VARCHAR(20) NULL COMMENT '建立資料的使用者 ID',
    `modify_user` VARCHAR(20) NULL COMMENT '修改資料的使用者 ID',
    PRIMARY KEY (`id`)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用戶表';

