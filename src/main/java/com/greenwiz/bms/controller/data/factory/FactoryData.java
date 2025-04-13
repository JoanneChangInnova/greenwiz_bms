package com.greenwiz.bms.controller.data.factory;

import com.greenwiz.bms.controller.data.kraken.KrakenData;
import com.greenwiz.bms.controller.data.user.UserData;
import com.greenwiz.bms.entity.Factory;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Delegate;

import java.util.List;
@Builder
@Data
public class FactoryData{
    @Delegate
    private Factory factory;
    private List<KrakenData> factoryKrakens;  // 關聯的 Kraken 設備
    private List<UserData> factoryCustomers;  // 關聯的用戶 ID 列表
    private String agentDisplayText;
}
