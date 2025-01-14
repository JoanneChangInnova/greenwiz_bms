package com.greenwiz.bms.controller.data.user;

import com.greenwiz.bms.entity.Language;
import com.greenwiz.bms.enumeration.Country;
import com.greenwiz.bms.enumeration.UserRole;
import com.greenwiz.bms.enumeration.UserState;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * AddUserRequest 和 UpdateUserRequest 共用的欄位
 */
@Data
public class UserRequest {
    /**
     * 角色
     */
    @NotNull(message = "角色不能為空")
    private UserRole role;

    /**
     * 父帳號 ID，對應其他用戶的 ID
     */
    private Long parentId;

    /**
     * 電子郵件地址
     */
    @NotBlank(message = "電子郵件地址不能為空")
    @Size(max = 255, message = "電子郵件長度為255字")
    private String email;

    /**
     * 使用者名稱
     */
    @NotBlank(message = "使用者名稱不能為空")
    @Size(min = 1, max = 128, message = "使用者名稱長度為1~128字")
    private String username;

    /**
     * 公司名稱
     */
    @Size(max = 100, message = "公司名稱長度為100字")
    private String company;

    /**
     * 聯絡人名稱
     */
    @Size(max = 255, message = "聯絡人名稱長度為255字")
    private String contact;

    /**
     * 電話的國家區號
     */
    private String phoneCountryCode;

    /**
     * 電話號碼
     */
    @Size(max = 16, message = "電話號碼長度為16")
    private String phoneNumber;

    /**
     * 使用者的國家
     */
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
    @NotNull(message = "最大設備數量不能為空")
    private Integer maxDevice = 20;

    /**
     * 語言偏好，例如 CHT 或 ENG
     */
    private Language language;
}
