package com.greenwiz.bms.controller;

import com.greenwiz.bms.controller.data.base.LayuiTableResp;
import com.greenwiz.bms.controller.data.base.PageReq;
import com.greenwiz.bms.controller.data.base.RestApiReq;
import com.greenwiz.bms.controller.data.user.AddUserReq;
import com.greenwiz.bms.controller.data.user.ParentData;
import com.greenwiz.bms.controller.data.user.UpdateUserReq;
import com.greenwiz.bms.entity.User;
import com.greenwiz.bms.service.UserService;
import com.greenwiz.bms.service.impl.UserServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/user")
public class UserController {

    @Autowired
    private UserServiceImpl userService;

    @PostMapping("/save")
    public void batachSaveForUser() {
        User user = new User();
        user.setRole(0);
        userService.save(user);
    }

    @GetMapping("/listParentIdAndUserName")
    public List<ParentData> listParentIdAndUserName() {
        List<ParentData> dataList = new ArrayList<>();
        dataList.add(new ParentData(1, "管理者A"));
        dataList.add(new ParentData(2, "管理者B"));
        dataList.add(new ParentData(3, "管理者C"));

        return dataList;
    }

    @PostMapping("/add")
    public ResponseEntity<?> addUser(@Validated @RequestBody AddUserReq addUserReq) {
        try {
            User addedUser = userService.addUser(addUserReq);
            return ResponseEntity.status(HttpStatus.CREATED).body(addedUser);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            log.error("新增用戶失敗: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("新增用戶失敗，請稍後重試");
        }
    }

    @PostMapping("/list")
    public LayuiTableResp<User> listUser(@RequestBody PageReq pageReq) {
        Page<User> users = userService.listUser(pageReq);
        return LayuiTableResp.success(users.getTotalElements(), users.getContent());
    }


    // 編輯用戶
    @PostMapping("/update")
    public ResponseEntity<String> updateUser(@RequestBody UpdateUserReq updateUserReq) {
        try {
            User user = userService.findByPk(updateUserReq.getId());
            BeanUtils.copyProperties(updateUserReq, user);
            User saved = userService.save(user);
            return ResponseEntity.ok("用戶資料更新成功");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("更新失敗");
        }
    }

    // 根據 ID 獲取用戶資料
    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        User user = userService.findByPk(id);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        return ResponseEntity.ok(user);
    }
}
