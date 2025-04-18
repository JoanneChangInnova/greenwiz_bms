package com.greenwiz.bms.controller.data.user;

import com.greenwiz.bms.controller.data.factory.FactoryBasicData;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Set;

@EqualsAndHashCode(callSuper = true)
@Data
public class GetUserData extends BaseUser {

    private UserData parentData;

    private Set<FactoryBasicData> factoryData; // 一般用戶綁定的工廠資訊
}
