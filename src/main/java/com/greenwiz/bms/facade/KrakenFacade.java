package com.greenwiz.bms.facade;

import com.greenwiz.bms.controller.data.kraken.AddKrakenReq;
import com.greenwiz.bms.controller.data.kraken.KrakenData;
import com.greenwiz.bms.controller.data.kraken.ListKrakenReq;
import com.greenwiz.bms.controller.data.kraken.UpdateKrakenReq;
import com.greenwiz.bms.entity.Kraken;
import com.greenwiz.bms.exception.BmsException;
import com.greenwiz.bms.service.KrakenService;
import com.greenwiz.bms.utils.ValidationUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static com.greenwiz.bms.enumeration.ResultCode.NAME_DUPLICATE;
import static com.greenwiz.bms.enumeration.ResultCode.VERSION_INVALID;

@Service
public class KrakenFacade {

    @Autowired
    private KrakenService krakenService;

    public Page<Kraken> getKrakenList(ListKrakenReq listKrakenReq) {
        ExampleMatcher matcher = ExampleMatcher.matching().withIgnoreNullValues()
                .withMatcher("krakenModel", ExampleMatcher.GenericPropertyMatchers.contains())
                .withMatcher("name", ExampleMatcher.GenericPropertyMatchers.contains())
                .withMatcher("dtInstall", ExampleMatcher.GenericPropertyMatchers.contains())
                .withMatcher("factoryKrakenSerial", ExampleMatcher.GenericPropertyMatchers.contains());
        Kraken kraken = new Kraken();
        BeanUtils.copyProperties(listKrakenReq, kraken);
        Example<Kraken> example = Example.of(kraken, matcher);
        //加上時間條件 轉換成Spec
        return krakenService.getKrakenPageBySpecification(example, listKrakenReq.getPageable());
    }

    public void addKraken(AddKrakenReq request) {
        Kraken kraken = new Kraken();
        BeanUtils.copyProperties(request, kraken);
        krakenService.save(kraken);
    }

    public void updateKraken(UpdateKrakenReq request) {
        Kraken kraken = krakenService.findByPk(request.getId());
        ValidationUtils.validateVersion(request.getDtModify(),kraken.getDtModify());
        kraken.setKrakenModel(request.getKrakenModel());
        kraken.setFactoryIotSerial(request.getFactoryIotSerial());
        kraken.setName(request.getName());
        kraken.setState(request.getState());
        kraken.setFwVer(request.getFwVer());
        kraken.setDtInstall(request.getDtInstall());
        krakenService.save(kraken);
    }

    public List<KrakenData> listKrakenData() {
        List<KrakenData> krakenList = krakenService.findAllKrakenData();
        return krakenList;
    }

    /**
     * 獲取未綁定工廠的 Kraken 列表
     */
    public List<KrakenData> listUnboundKrakenData() {
        return krakenService.findByFactoryIdIsNull().stream()
                .map(KrakenData::convertToKrakenData)
                .collect(Collectors.toList());
    }
}
