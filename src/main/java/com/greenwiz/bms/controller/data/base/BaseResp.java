package com.greenwiz.bms.controller.data.base;

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

    private Long createUser;


    private LocalDateTime dtCreate;


    private Long modifyUser;


    private LocalDateTime dtModify;
}
