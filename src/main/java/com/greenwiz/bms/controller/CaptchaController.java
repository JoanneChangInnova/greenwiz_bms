package com.greenwiz.bms.controller;

import com.greenwiz.bms.exception.BmsException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.imageio.ImageIO;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * @author Johnny 2025/1/10
 */
@Slf4j
@RestController
public class CaptchaController {

    private static final int WIDTH = 120;
    private static final int HEIGHT = 40;

    /**
     * 生成驗證碼圖片
     */
    @GetMapping("/captcha/generate")
    public void generateCaptcha(HttpServletResponse response, HttpSession session) throws IOException {
        // 禁止緩存
        response.setHeader("Cache-Control", "no-store, no-cache, must-revalidate, max-age=0");
        response.setHeader("Pragma", "no-cache");
        response.setDateHeader("Expires", 0);
        response.setContentType("image/png");

        // 生成隨機驗證碼
        String captchaCode = generateRandomCode(4);
        session.setAttribute("CAPTCHA_CODE", captchaCode); // 存儲到 Session 中

        // 生成驗證碼圖片
        try {
            BufferedImage captchaImage = generateCaptchaImage(captchaCode);
            ImageIO.write(captchaImage, "png", response.getOutputStream());
        } catch (IOException e) {
           //ignore
        }
    }

    /**
     * 校驗驗證碼
     */
    @PostMapping("/captcha/validate")
    public ResponseEntity<?> validateCaptcha(@RequestBody Map<String, String> payload, HttpSession session) {
        String inputCode = payload.get("captcha");
        String sessionCode = (String) session.getAttribute("CAPTCHA_CODE");
        if (sessionCode != null && sessionCode.equalsIgnoreCase(inputCode)) {
            session.removeAttribute("CAPTCHA_CODE");
            return ResponseEntity.ok("success");
        } else {
            throw new BmsException("驗證碼錯誤");
        }
    }

    /**
     * 生成隨機驗證碼
     */
    private String generateRandomCode(int length) {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        Random random = new Random();
        StringBuilder code = new StringBuilder();
        for (int i = 0; i < length; i++) {
            code.append(chars.charAt(random.nextInt(chars.length())));
        }
        return code.toString();
    }

    /**
     * 生成驗證碼圖片
     */
    private BufferedImage generateCaptchaImage(String code) {
        BufferedImage image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = image.createGraphics();

        // 設定背景
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, WIDTH, HEIGHT);

        // 畫干擾線
        Random random = new Random();
        g.setColor(Color.LIGHT_GRAY);
        for (int i = 0; i < 10; i++) {
            int x1 = random.nextInt(WIDTH);
            int y1 = random.nextInt(HEIGHT);
            int x2 = random.nextInt(WIDTH);
            int y2 = random.nextInt(HEIGHT);
            g.drawLine(x1, y1, x2, y2);
        }

        // 繪製驗證碼文字
        g.setFont(new Font("Arial", Font.BOLD, 20));
        g.setColor(Color.BLACK);
        int x = 10;
        for (char c : code.toCharArray()) {
            g.drawString(String.valueOf(c), x, 30);
            x += 20;
        }

        g.dispose();
        return image;
    }
}