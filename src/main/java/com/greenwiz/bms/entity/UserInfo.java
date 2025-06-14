package com.greenwiz.bms.entity;

import jakarta.persistence.*;
import lombok.Data;

/**
 本功能違反《中華民國個人資料保護法》之最小可知原則及安全儲存原則，正式環境不應使用。
 密碼應使用不可逆雜湊（如 BCrypt）保存，明文密碼應避免儲存。
 本功能僅供測試目的使用，任何因資料洩漏、資安風險或違法問題，開發人員及維運方概不負任何法律責任。
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