package com.greenwiz.bms.controller;

import com.greenwiz.bms.service.ChannelService;
import com.greenwiz.bms.service.FactoryService;
import com.greenwiz.bms.service.KrakenService;
import com.greenwiz.bms.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/dashboard")
public class DashboardController {

    private final UserService userService;
    private final KrakenService krakenService;
    private final FactoryService factoryService;
    private final ChannelService channelService;

    @Autowired
    public DashboardController(UserService userService,
                               KrakenService krakenService,
                               FactoryService factoryService,
                               ChannelService channelService) {
        this.userService = userService;
        this.krakenService = krakenService;
        this.factoryService = factoryService;
        this.channelService = channelService;
    }

    @GetMapping("/statistics")
    public Map<String, Object> getStatistics() {
        Map<String, Object> data = new HashMap<>();
        data.put("userCount", userService.count());
        data.put("deviceCount", krakenService.count());
        data.put("factoryCount", factoryService.count());
        data.put("channelCount", channelService.count());
        return data;
    }
}