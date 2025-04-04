package com.greenwiz.bms.entity;

import com.greenwiz.bms.utils.ThreadLocalUtils;
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
     * 建立資料的使用者 id
     */
    private Long createUser;

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
     * 修改資料的使用者 id
     */
    private Long modifyUser;

    @PrePersist
    private void onPrePersist() {
        setCreateUser(ThreadLocalUtils.getUser().getId());
        setModifyUser(ThreadLocalUtils.getUser().getId());
        setDtCreate(LocalDateTime.now());
    }

    @PreUpdate
    private void insertModifyUser() {
        setModifyUser(ThreadLocalUtils.getUser().getId());
    }


}
