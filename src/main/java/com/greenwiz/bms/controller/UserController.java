package com.greenwiz.bms.controller;

import com.greenwiz.bms.controller.data.user.ParentData;
import com.greenwiz.bms.entity.User;
import com.greenwiz.bms.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/user")
public class UserController {

    @Autowired
    private UserService userService;

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
}
