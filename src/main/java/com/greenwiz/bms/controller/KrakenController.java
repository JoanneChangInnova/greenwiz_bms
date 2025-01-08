package com.greenwiz.bms.controller;

import com.greenwiz.bms.controller.data.base.LayuiTableResp;
import com.greenwiz.bms.controller.data.base.PageReq;
import com.greenwiz.bms.controller.data.kraken.AddKrakenReq;
import com.greenwiz.bms.controller.data.kraken.ListKrakenReq;
import com.greenwiz.bms.controller.data.kraken.UpdateKrakenReq;
import com.greenwiz.bms.entity.Kraken;
import com.greenwiz.bms.facade.KrakenFacade;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/kraken")
public class KrakenController {

    @Autowired
    private KrakenFacade krakenFacade;

    @PostMapping("/list")
    public LayuiTableResp<Kraken> getKrakenList(@RequestBody ListKrakenReq request) {
        Page<Kraken> krakens = krakenFacade.getKrakenList(request);
        return LayuiTableResp.success(krakens.getTotalElements(), krakens.getContent());
    }

    @PostMapping("/add")
    public ResponseEntity<?> addKraken(@RequestBody @Valid AddKrakenReq request) {
        krakenFacade.addKraken(request);
        return ResponseEntity.ok("新增成功");
    }

    @PostMapping("/update")
    public ResponseEntity<?> updateKraken(@RequestBody UpdateKrakenReq request) {
        krakenFacade.updateKraken(request);
        return ResponseEntity.ok("修改成功");
    }
}
