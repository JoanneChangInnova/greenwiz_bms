package com.greenwiz.bms.facade;

import com.greenwiz.bms.controller.data.factory.AddFactoryReq;
import com.greenwiz.bms.controller.data.factory.ListFactoryReq;
import com.greenwiz.bms.controller.data.factory.UpdateFactoryReq;
import com.greenwiz.bms.entity.Factory;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Service
public class FactoryFacade {

    public void addFactory(AddFactoryReq addFactoryReq) {

    }

    public Page<Factory> getFactoryList(ListFactoryReq request) {
        return null;
    }

    public void updateFactory(Long id, UpdateFactoryReq request) {

    }
}
