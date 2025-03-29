package com.greenwiz.bms.controller;

import com.greenwiz.bms.controller.data.base.LayuiTableResp;
import com.greenwiz.bms.controller.data.channel.AddChannelReq;
import com.greenwiz.bms.controller.data.channel.ListChannelReq;
import com.greenwiz.bms.controller.data.channel.UpdateChannelReq;
import com.greenwiz.bms.controller.data.kraken.ListKrakenReq;
import com.greenwiz.bms.entity.Channel;
import com.greenwiz.bms.entity.Kraken;
import com.greenwiz.bms.facade.ChannelFacade;
import com.greenwiz.bms.service.ChannelService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/channel")
public class ChannelController {

    @Autowired
    private ChannelFacade channelFacade;

    @Autowired
    private ChannelService channelService;

    @PostMapping("/add")
    public ResponseEntity<?> addChannel(@Valid @RequestBody AddChannelReq request) {
        channelFacade.addChannel(request);
        return ResponseEntity.ok("新增成功");
    }

    @PostMapping("/list")
    public LayuiTableResp<Channel> getKrakenList(@RequestBody ListChannelReq request) {
        Page<Channel> channels = channelFacade.getChannelList(request);
        return LayuiTableResp.success(channels.getTotalElements(), channels.getContent());
    }
    @GetMapping("/{id}")
    public ResponseEntity<?> getChannel(@PathVariable Long id) {
        Channel channel = channelService.findByPk(id);
        if(channel == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        return ResponseEntity.ok(channel);
    }
    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateChannel(@PathVariable Long id, @Valid @RequestBody UpdateChannelReq request) {
        channelFacade.updateChannel(id, request);
        return ResponseEntity.ok("修改成功");
    }
}
