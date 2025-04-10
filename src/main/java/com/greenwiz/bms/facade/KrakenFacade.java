package com.greenwiz.bms.facade;

import com.greenwiz.bms.controller.data.kraken.*;
import com.greenwiz.bms.entity.Kraken;
import com.greenwiz.bms.entity.User;
import com.greenwiz.bms.enumeration.UserRole;
import com.greenwiz.bms.exception.BmsException;
import com.greenwiz.bms.service.KrakenService;
import com.greenwiz.bms.service.UserService;
import com.greenwiz.bms.utils.ValidationUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static com.greenwiz.bms.enumeration.ResultCode.NAME_DUPLICATE;
import static com.greenwiz.bms.enumeration.ResultCode.VERSION_INVALID;

@Service
public class KrakenFacade {

    @Autowired
    private KrakenService krakenService;

    @Autowired
    private UserFacade userFacade;

    @Autowired
    private UserService userService;

    public Page<ListKrakenData> getKrakenList(ListKrakenReq listKrakenReq) {
        ExampleMatcher matcher = ExampleMatcher.matching().withIgnoreNullValues()
                .withMatcher("krakenModel", ExampleMatcher.GenericPropertyMatchers.contains())
                .withMatcher("name", ExampleMatcher.GenericPropertyMatchers.contains())
                .withMatcher("dtInstall", ExampleMatcher.GenericPropertyMatchers.contains())
                .withMatcher("factoryKrakenSerial", ExampleMatcher.GenericPropertyMatchers.contains());
        Kraken kraken = new Kraken();
        BeanUtils.copyProperties(listKrakenReq, kraken);
        Example<Kraken> example = Example.of(kraken, matcher);
        
        // 獲取原始分頁數據
        Page<Kraken> krakenPage = krakenService.getKrakenPageBySpecification(example, listKrakenReq.getPageable());
        
        // 轉換為 ListKrakenData 並設置 createModifyUser
        // Todo: N+1
        List<ListKrakenData> krakenDataList = krakenPage.getContent().stream()
                .map(k -> {
                    ListKrakenData data = new ListKrakenData();
                    BeanUtils.copyProperties(k, data);
                    data.setCreateModifyUser(userFacade.buildCreateModifyUser(k.getCreateUser(), k.getModifyUser()));

                    // 設置擁有者資訊
                    if (k.getUserId() != null) {
                        User owner = userService.findByPk(k.getUserId());
                        if (owner != null) {
                            String ownerInfo = owner.getUsername()+ " (" + owner.getEmail() + ")";
                            data.setOwnerUserInfo(ownerInfo);
                        }
                    }

                    return data;
                })
                .collect(Collectors.toList());

        return new PageImpl<>(krakenDataList, krakenPage.getPageable(), krakenPage.getTotalElements());
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

    public void assignKraken(AssignKrakenReq request) {
        List<Kraken> krakens = krakenService.findByIdIn(request.getKrakenIds());
        for (Long id : request.getKrakenIds()) {
            if (krakens.stream().noneMatch(k -> k.getId().equals(id))) {
                throw new BmsException("Kraken ID " + id + "不存在");
            }
        }
        User user = userService.findByPk(request.getUserId());
        if (user == null) {
            throw new BmsException("User不存在");
        }
        if(!user.getRole().equals(request.getUserRole())){
            throw new BmsException("User角色不匹配");
        }
        for(Kraken kraken : krakens){
            kraken.setUserId(request.getUserId());
            kraken.setUserRole(request.getUserRole());
        }
        krakenService.saveAllAndFlush(krakens);
    }
}
