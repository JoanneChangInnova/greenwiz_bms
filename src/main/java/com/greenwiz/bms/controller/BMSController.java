package com.greenwiz.bms.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
@Controller
public class BMSController {

    @GetMapping("/")
    public String loginPage() {
        // 若沒有使用者資訊需登入
        return "redirect:/login.html";
        // 若有使用者資訊，導航到index
    }
}
