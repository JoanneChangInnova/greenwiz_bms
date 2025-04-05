package com.greenwiz.bms.entity;

import com.greenwiz.bms.enumeration.Country;
import com.greenwiz.bms.enumeration.UserRole;
import com.greenwiz.bms.enumeration.UserState;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@Entity
public class User extends BaseEntity {

    @Id
    private Long id;

    /**
     * 角色代碼: 0:admin, 1:agent, 2:installer, 3:customer
     */
    private UserRole role;

    /**
     * 父帳號 ID，對應其他用戶的 ID
     */
    private Long parentId;

    /**
     * 代理商 ID，除了ADMIN其他角色必填
     * 同一個代理商底下的 安裝商和用戶，其agentId相同，
     * 方便查找出同一群組（同一個代理商）的所有用戶id
     */
    private Long agentId;

    /**
     * 電子郵件地址
     */
    private String email;

    /**
     * 加密密碼
     */
    private String password;

    /**
     * 使用者名稱
     */
    private String username;

    /**
     * 公司名稱
     */
    private String company;

    /**
     * 聯絡人名稱
     */
    private String contact;

    /**
     * 電話國碼
     */
    private String phoneCountryCode;

    /**
     * 電話號碼
     */
    private String phoneNumber;

    /**
     * 使用者的國家
     */
    @Enumerated(EnumType.STRING)
    private Country country;

    /**
     * 平臺等級
     */
    private Integer level;

    /**
     * 使用者的地址
     */
    private String address;

    /**
     * 用戶狀態: 0:待審, 1:開通, 2:封鎖
     */
    private UserState state;

    /**
     * 最大設備數量，默認 20
     */
    private Integer maxDevice;

    /**
     * 語言偏好，例如 CHT 或 ENG
     */
    @Enumerated(EnumType.STRING)
    private Language language;
}