package com.greenwiz.bms.controller;

import com.greenwiz.bms.controller.data.base.LayuiTableResp;
import com.greenwiz.bms.controller.data.kraken.*;
import com.greenwiz.bms.entity.Kraken;
import com.greenwiz.bms.entity.User;
import com.greenwiz.bms.facade.KrakenFacade;
import com.greenwiz.bms.service.KrakenService;
import com.greenwiz.bms.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/v1/kraken")
public class KrakenController {

    @Autowired
    private KrakenFacade krakenFacade;

    @Autowired
    private KrakenService krakenService;

    @Autowired
    private UserService userService;

    @GetMapping("/{id}")
    public ResponseEntity<Kraken> getKraken(@PathVariable Long id) {
        Kraken kraken = krakenService.findByPk(id);
        if (kraken == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        return ResponseEntity.ok(kraken);
    }

    @PostMapping("/list")
    public LayuiTableResp<ListKrakenData> getKrakenList(@RequestBody ListKrakenReq request) {
        Page<ListKrakenData> krakens = krakenFacade.getKrakenList(request);
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

    /**
     * 列出所有未綁定 Factory 的 kraken ID, kraken name
     */
    @GetMapping("/listKrakenData")
    public List<KrakenData> listKrakenData() {
        return krakenFacade.listUnboundKrakenData();
    }

    /**
     * 以agnetId為條件
     * 列出所有未綁定 Factory 的 KrakenData
     * 查出User中agentId相同的用戶列表
     * 查出該用戶列表的所有Kraken
     * 且未綁定工廠的Kraken列表
     */
    @GetMapping("/listKrakenData/{agentId}")
    public List<KrakenData> listKrakenDataByAgentId(@PathVariable Long agentId) {
        // 1. 查詢該代理商下的所有用戶
        List<User> users = userService.findByAgentId(agentId);
        
        // 2. 獲取這些用戶的 ID 列表
        List<Long> userIds = users.stream()
                .map(User::getId)
                .toList();
        
        // 3. 查詢這些用戶擁有的未綁定 Kraken
        List<KrakenData> krakenDataList = krakenService.findByUserIdsAndFactoryIdIsNull(userIds);
        return krakenDataList;
    }


    @PostMapping("/assign")
    public ResponseEntity<?> assignKraken(@RequestBody AssignKrakenReq request) {
        krakenFacade.assignKraken(request);
        return ResponseEntity.ok("派發成功");
    }


}
