package com.greenwiz.bms.service;

import com.greenwiz.bms.controller.data.base.PageReq;
import com.greenwiz.bms.controller.data.user.UserData;
import com.greenwiz.bms.entity.User;
import com.greenwiz.bms.enumeration.UserRole;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;
import java.util.stream.Collectors;

public interface UserService extends BaseDomainService<Long, User> {

    Long getNextUserId();

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

    User findByEmail(String email);

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

    List<User> findByUserRole(UserRole userRole);

    List<User> findByParentIdIn(List<Long> parentIds);

    void updatePassword(Long userId, String encodedNewPassword);


    List<UserData> findAllUserDataByRoleCustomer();

    Long findAgentIdByUserId(Long userId);

    List<User> findByAgentIdAndUserRole(Long agentId, UserRole userRole);

    List<User> findCustomersWithLockByAgentId(Long oldAgentId);

    UserData getUserDataByUserId(Long userId);

    List<UserData> findByRole(UserRole role);

    List<User> findByAgentId(Long agentId);

    /**
     * 根據用戶ID列表查詢用戶數據
     * @param userIds 用戶ID列表
     * @return 用戶數據列表
     */
    List<UserData> findUserDataListByIds(List<Long> userIds);

    Page<User> findAll(Specification<User> spec, PageRequest pageable);

    Long count();
}
