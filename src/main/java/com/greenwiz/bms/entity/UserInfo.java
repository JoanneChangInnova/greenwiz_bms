package com.greenwiz.bms.entity;

import jakarta.persistence.*;
import lombok.Data;

/**
 * @author Johnny 2025/6/7
 *  低能需求
 */

@Entity
@Table(name = "user_info")
@Data
    public class UserInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 關聯主 User 表的 ID
    @Column(nullable = false)
    private Long userId;

    // 明文或同步密碼（依需求）
    @Column(nullable = false)
    private String pwd;
}