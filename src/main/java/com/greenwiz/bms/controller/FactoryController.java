package com.greenwiz.bms.controller;

import com.greenwiz.bms.controller.data.base.LayuiTableResp;
import com.greenwiz.bms.controller.data.factory.*;
import com.greenwiz.bms.controller.data.kraken.KrakenData;
import com.greenwiz.bms.entity.Factory;
import com.greenwiz.bms.facade.FactoryFacade;
import com.greenwiz.bms.facade.KrakenFacade;
import com.greenwiz.bms.facade.UserFactoryFacade;
import com.greenwiz.bms.service.FactoryService;
import com.greenwiz.bms.service.KrakenService;
import com.greenwiz.bms.service.UserFactoryService;
import com.greenwiz.bms.service.UserService;
import com.greenwiz.bms.utils.ThreadLocalUtils;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping("/api/v1/factory")
public class FactoryController {
    @Autowired
    private FactoryFacade factoryFacade;

    @Autowired
    private FactoryService factoryService;

    @Autowired
    private KrakenFacade krakenFacade;

    @Autowired
    private UserFactoryFacade userFactoryFacade;

    @Autowired
    private UserService userService;

    @Autowired
    private KrakenService krakenService;

    @Autowired
    private UserFactoryService userFactoryService;

    @PostMapping("/add")
    @Transactional(Transactional.TxType.REQUIRED)
    public ResponseEntity<?> addFactory(@Valid @RequestBody AddFactoryReq request) {
        factoryFacade.addFactory(request);
        return ResponseEntity.ok("新增成功");
    }

    @PostMapping("/list")
    public LayuiTableResp<ListFactoryData> getFactoryList(@RequestBody ListFactoryReq request) {
        Page<Factory> factories = factoryFacade.getFactoryList(request, ThreadLocalUtils.getUser().getRole());
        Page<ListFactoryData> listFactoryData = ListFactoryData.convertToListFactoryData(
            factories,
            userService,
            krakenService,
            userFactoryService
        );
        return LayuiTableResp.success(listFactoryData.getTotalElements(), listFactoryData.getContent());
    }

    @GetMapping("/{id}")
    public ResponseEntity<FactoryData> getFactory(@PathVariable Long id) {
        FactoryData factoryData = factoryFacade.getFactory(id);
        return ResponseEntity.ok(factoryData);
    }

    @PostMapping("/update/{id}")
    @Transactional(Transactional.TxType.REQUIRED)
    public ResponseEntity<?> updateFactory(@PathVariable Long id, @Valid @RequestBody UpdateFactoryReq request) {
        factoryFacade.updateFactory(id, request);
        return ResponseEntity.ok("修改成功");
    }

    //findByInstallerId
    @GetMapping("/findByInstallerId/{installerId}")
    public ResponseEntity<Set<FactoryBasicData>> findByInstallerId(@PathVariable Long installerId) {
        Set<FactoryBasicData> factoryData = factoryFacade.findByInstallerId(installerId);
        return ResponseEntity.ok(factoryData);
    }
}
