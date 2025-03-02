package com.greenwiz.bms.controller;

import com.greenwiz.bms.controller.data.channel.AddChannelReq;
import com.greenwiz.bms.facade.ChannelFacade;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/channel")
public class ChannelController {

    @Autowired
    private ChannelFacade channelFacade;

    @PostMapping("/add")
    public ResponseEntity<?> addChannel(@Valid @RequestBody AddChannelReq request) {
        channelFacade.addChannel(request);
        return ResponseEntity.ok("新增成功");
    }
}
