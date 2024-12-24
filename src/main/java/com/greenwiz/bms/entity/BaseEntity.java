package com.greenwiz.bms.entity;

import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.Version;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@MappedSuperclass
public class BaseEntity {

    public static final String DEFAULT_USER_SYSTEM = "SYSTEM";

    /**
     * 建立資料的使用者 ID 或 SYSTEM
     */
    private String createUser = "SYSTEM";

    /**
     * 最後修改時間
     */
    @Version
    private LocalDateTime dtModify;

    /**
     * 註冊時間
     */
    private LocalDateTime dtCreate = LocalDateTime.now();

    /**
     * 修改資料的使用者 ID 或 SYSTEM
     */
    private String modifyUser = "SYSTEM";

}
