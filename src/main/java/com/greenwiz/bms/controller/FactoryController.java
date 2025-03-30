package com.greenwiz.bms.controller;

import com.greenwiz.bms.controller.data.base.LayuiTableResp;
import com.greenwiz.bms.controller.data.channel.AddChannelReq;
import com.greenwiz.bms.controller.data.channel.ListChannelReq;
import com.greenwiz.bms.controller.data.channel.UpdateChannelReq;
import com.greenwiz.bms.controller.data.factory.AddFactoryReq;
import com.greenwiz.bms.controller.data.factory.ListFactoryReq;
import com.greenwiz.bms.controller.data.factory.UpdateFactoryReq;
import com.greenwiz.bms.entity.Channel;
import com.greenwiz.bms.entity.Factory;
import com.greenwiz.bms.facade.ChannelFacade;
import com.greenwiz.bms.facade.FactoryFacade;
import com.greenwiz.bms.service.ChannelService;
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

    @PostMapping("/add")
    public ResponseEntity<?> addChannel(@Valid @RequestBody AddFactoryReq request) {
        factoryFacade.addFactory(request);
        return ResponseEntity.ok("新增成功");
    }

    @PostMapping("/list")
    public LayuiTableResp<Factory> getKrakenList(@RequestBody ListFactoryReq request) {
        Page<Factory> channels = factoryFacade.getFactoryList(request);
        return LayuiTableResp.success(channels.getTotalElements(), channels.getContent());
    }
    @GetMapping("/{id}")
    public ResponseEntity<?> getChannel(@PathVariable Long id) {
        Factory channel = factoryService.findByPk(id);
        if(channel == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        return ResponseEntity.ok(channel);
    }
    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateChannel(@PathVariable Long id, @Valid @RequestBody UpdateFactoryReq request) {
        factoryFacade.updateFactory(id, request);
        return ResponseEntity.ok("修改成功");
    }
}
