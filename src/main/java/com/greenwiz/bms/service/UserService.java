package com.greenwiz.bms.service;

import com.greenwiz.bms.controller.data.base.PageReq;
import com.greenwiz.bms.controller.data.user.AddUserReq;
import com.greenwiz.bms.entity.User;
import org.springframework.data.domain.Page;

public interface UserService extends BaseDomainService<Long, User> {

    /**
     * 分頁列出用戶
     * @param pageReq 分頁請求參數
     * @return 分頁用戶列表
     */
    Page<User> listUser(PageReq pageReq);


    /**
     * 根據用戶名查詢用戶
     * @param username 用戶名
     * @return 查詢到的用戶實體，如果不存在則返回 null
     */
    User findByUsername(String username);

    /**
     * 驗證用戶密碼
     * @param rawPassword 明文密碼
     * @param encodedPassword 加密後的密碼
     * @return 是否匹配
     */
    boolean validatePassword(String rawPassword, String encodedPassword);


    /**
     * 密碼加密
     * @param rawPassword 明文密碼
     * @return 加密後的密碼
     */
    String encodePassword(String rawPassword);

}
