package com.greenwiz.bms.controller;

import com.greenwiz.bms.entity.User;
import com.greenwiz.bms.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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

    @PostMapping("/batachSaveForUser")
    public void batachSaveForUser() {
        List<User> users = new ArrayList<>();

        for (int i = 0; i < 1; i++) {
            User user = new User();
            user.setRole(0);
            users.add(user);
        }
        userService.saveAll(users);
    }
}
