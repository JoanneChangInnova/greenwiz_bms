package com.greenwiz.bms.controller;

import com.greenwiz.bms.controller.data.base.LayuiTableResp;
import com.greenwiz.bms.controller.data.base.PageReq;
import com.greenwiz.bms.controller.data.user.*;
import com.greenwiz.bms.entity.User;
import com.greenwiz.bms.enumeration.UserRole;
import com.greenwiz.bms.exception.BmsException;
import com.greenwiz.bms.facade.UserFacade;
import com.greenwiz.bms.service.UserService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/user")
public class UserController {

    @Autowired
    private UserFacade userFacade;

    @Autowired
    private UserService userService;

    @PostMapping("/listParentInfo")
    public List<ParentData> listParentInfo(@RequestBody UserRoleRequest request) {
        return userFacade.listParentInfo(request.getUserRole());
    }

    @PostMapping("/add")
    public ResponseEntity<?> addUser(@Valid @RequestBody AddUserReq addUserReq) {
        User addedUser = userFacade.addUser(addUserReq);
        return ResponseEntity.status(HttpStatus.CREATED).body(addedUser);
    }

    @PostMapping("/list")
    public LayuiTableResp<UserListData> listUser(@RequestBody PageReq pageReq) {
        Page<UserListData> users = userFacade.listUser(pageReq);
        return LayuiTableResp.success(users.getTotalElements(), users.getContent());
    }


    @PostMapping("/update")
    public ResponseEntity<String> updateUser(@RequestBody @Valid UpdateUserReq updateUserReq) {
        userFacade.updateUser(updateUserReq);
        return ResponseEntity.ok("用戶資料更新成功");
    }

    @GetMapping("/{id}")
    public ResponseEntity<GetUserData> getUserById(@PathVariable Long id) {
        GetUserData getUserData = userFacade.getUserById(id);
        return ResponseEntity.ok(getUserData);
    }
}
