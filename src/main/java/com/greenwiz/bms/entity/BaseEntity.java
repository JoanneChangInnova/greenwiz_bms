package com.greenwiz.bms.entity;

import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Version;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@MappedSuperclass
public class BaseEntity {

    /**
     * 建立資料的使用者 ID 或 SYSTEM
     */
    private String createUser;

    /**
     * 最後修改時間
     */
    @Version
    private LocalDateTime dtModify;

    /**
     * 註冊時間
     */
    private LocalDateTime dtCreate;

    /**
     * 修改資料的使用者 ID 或 SYSTEM
     */
    private String modifyUser;

    @PrePersist
    private void onPrePersist() {
        setCreateUser("SYSTEM");
        setModifyUser("SYSTEM");
        setDtCreate(LocalDateTime.now());
    }

    @PreUpdate
    private void insertModifyUser() {
        setModifyUser("SYSTEM");
    }


}
