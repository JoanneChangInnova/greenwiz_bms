package com.greenwiz.bms.controller;

import com.greenwiz.bms.controller.data.base.LayuiTableResp;
import com.greenwiz.bms.controller.data.factory.AddFactoryReq;
import com.greenwiz.bms.controller.data.factory.ListFactoryReq;
import com.greenwiz.bms.controller.data.factory.UpdateFactoryReq;
import com.greenwiz.bms.entity.Factory;
import com.greenwiz.bms.facade.FactoryFacade;
import com.greenwiz.bms.facade.KrakenFacade;
import com.greenwiz.bms.service.FactoryService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/factory")
public class FactoryController {
    @Autowired
    private FactoryFacade factoryFacade;

    @Autowired
    private FactoryService factoryService;

    @Autowired
    private KrakenFacade krakenFacade;

    @PostMapping("/add")
    public ResponseEntity<?> addFactory(@Valid @RequestBody AddFactoryReq request) {
        factoryFacade.addFactory(request);
        return ResponseEntity.ok("新增成功");
    }

    @PostMapping("/list")
    public LayuiTableResp<Factory> getFactoryList(@RequestBody ListFactoryReq request) {
        Page<Factory> factorys = factoryFacade.getFactoryList(request);
        return LayuiTableResp.success(factorys.getTotalElements(), factorys.getContent());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getFactory(@PathVariable Long id) {
        Factory factory = factoryService.findByPk(id);
        if (factory == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        return ResponseEntity.ok(factory);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateFactory(@PathVariable Long id, @Valid @RequestBody UpdateFactoryReq request) {
        factoryFacade.updateFactory(id, request);
        return ResponseEntity.ok("修改成功");
    }
}
