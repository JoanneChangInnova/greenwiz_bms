package com.greenwiz.bms.controller.data.user.base;

import com.greenwiz.bms.entity.BaseEntity;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class BaseResp {
    public BaseResp(BaseEntity baseEntity) {
        setCreateUser(baseEntity.getCreateUser());
        setModifyUser(baseEntity.getModifyUser());
        setDtCreate(baseEntity.getDtCreate());
        setDtModify(baseEntity.getDtModify());
    }

    private String createUser;


    private LocalDateTime dtCreate;


    private String modifyUser;


    private LocalDateTime dtModify;
}
