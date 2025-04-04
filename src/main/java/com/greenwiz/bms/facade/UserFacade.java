package com.greenwiz.bms.facade;

import com.greenwiz.bms.controller.data.base.PageReq;
import com.greenwiz.bms.controller.data.user.*;
import com.greenwiz.bms.entity.User;
import com.greenwiz.bms.enumeration.UserRole;
import com.greenwiz.bms.enumeration.UserState;
import com.greenwiz.bms.exception.BmsException;
import com.greenwiz.bms.service.UserService;
import com.greenwiz.bms.utils.JwtUtils;
import com.greenwiz.bms.utils.ThreadLocalUtils;
import com.greenwiz.bms.utils.ValidationUtils;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
public class UserFacade {

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtils jwtUtils;

    public User addUser(AddUserReq addUserReq) {
        // 檢查用戶名是否已存在
        User user = userService.findByEmail(addUserReq.getEmail());
        if (user != null) {
            throw new BmsException("Email已存在，請選擇其他Email");
        }

        User newUser = new User();
        BeanUtils.copyProperties(addUserReq, newUser);

        // 密碼加密
        String encodedPassword = passwordEncoder.encode(addUserReq.getPassword());
        newUser.setPassword(encodedPassword);

        //設置默認值（如果未提供）
        if (newUser.getState() == null) {
            newUser.setState(UserState.APPROVED); // 管理員新增，默認狀態：開通
        }

        return userService.save(newUser);
    }

    /**
     *  管理員可看到所有用戶
     *  其他角色只可看到parentId為自己的用戶 (尚未實作）
     */
    public Page<UserListData> listUser(PageReq pageReq) {
        // 1. 獲取用戶分頁結果
        Page<User> userPage = userService.listUser(pageReq);

        // 2. 提取 parentIds（去重）
        List<Long> parentIds = userPage.getContent().stream()
                .map(User::getParentId) // 提取 parentId
                .filter(parentId -> parentId != null) // 過濾掉空值
                .distinct() // 去重
                .collect(Collectors.toList());

        // 3. 查詢父帳號信息
        List<User> parentList = userService.findByParentIdIn(parentIds);

        // 4. 將父帳號信息轉換為 Map<parentId, parentUserInfo>
        Map<Long, String> parentInfoMap = parentList.stream()
                .collect(Collectors.toMap(
                        User::getId,
                        user -> user.getUsername() + " (" + user.getEmail() + ")"
                ));

        // 5. 使用 BeanUtils.copyProperties 簡化屬性賦值
        List<UserListData> userListData = userPage.getContent().stream()
                .map(user -> {
                    UserListData data = new UserListData();
                    BeanUtils.copyProperties(user, data); // 自動拷貝相同名稱的屬性
                    data.setParentUserInfo(parentInfoMap.getOrDefault(user.getParentId(), "無")); // 手動設置額外字段
                    return data;
                })
                .collect(Collectors.toList());

        // 6. 返回轉換後的分頁結果
        return new PageImpl<>(userListData, userPage.getPageable(), userPage.getTotalElements());
    }

    public User updateUser(UpdateUserReq updateUserReq) {
        User user = userService.findByPk(updateUserReq.getId());

        ValidationUtils.validateVersion(updateUserReq.getDtModify(),user.getDtModify());

        //用戶名稱若修改，檢查是否已有同名用戶
        if(!updateUserReq.getEmail().equals(user.getEmail())){
            User userByEmail = userService.findByEmail(updateUserReq.getEmail());
            if(userByEmail != null){
                throw new BmsException("Email已存在，請選擇其他Email");
            }
        }

        BeanUtils.copyProperties(updateUserReq, user);
        return userService.save(user);
    }

    /**
     * 管理員可申請任何角色帳號：
     *  - 角色為管理員 -> 上層findByRole0
     *  - 角色為代理商 -> 上層findByRole0
     *  - 角色為安裝商 -> 上層findByRole1
     *  - 角色為客戶   -> 上層findByRole2
     * 代理商只能申請安裝商：上層只能填代理商自己
     * 安裝商可申請客戶，但需上層代理商或管理員審核：上層填安裝商自己
     */
    public List<UserData> listParentInfo(UserRole userRole) {
        // 獲取當前操作員的用戶
        User operator = findUserByIdOrFail(ThreadLocalUtils.getUser().getId());

        List<User> userList;

        if (operator.getRole() == UserRole.ADMIN) {
            // 若操作員是 ADMIN，根據傳入的 userRole 使用 switch 查詢對應角色的用戶
            userList = switch (userRole) {
                case ADMIN, AGENT -> userService.findByUserRole(UserRole.ADMIN);
                case INSTALLER -> userService.findByUserRole(UserRole.AGENT);
                case CUSTOMER -> userService.findByUserRole(UserRole.INSTALLER);
            };
        } else {
            // 若操作員不是 ADMIN，則只返回操作員自己
            userList = List.of(operator);
        }

        // 將 userList 轉換為 ParentData 列表
        return userList.stream()
                .map(user -> new UserData(user.getId(), user.getUsername(), user.getEmail()))
                .toList();
    }

    private User findUserByEmailOrFail(String email){
        User user = userService.findByEmail(email);
        if(user == null){
            throw new BmsException("User不存在");
        }
        return user;
    }

    private User findUserByIdOrFail(Long id){
        User user = userService.findByPk(id);
        if(user == null){
            throw new BmsException("User不存在");
        }
        return user;
    }

    public GetUserData getUserById(Long id) {
        User user = userService.findByPk(id);
        if (user == null) {
            throw new BmsException("找不到此用戶，id:" + id);
        }
        User parentUser = userService.findByPk(user.getParentId());
        if(parentUser == null){
            throw new BmsException("找不到管理者用戶，id:" + id);
        }
        UserData parentData = new UserData(parentUser.getId(), parentUser.getUsername(), parentUser.getEmail());
        GetUserData getUserData = new GetUserData();
        BeanUtils.copyProperties(user,getUserData);
        getUserData.setParentData(parentData);
        return getUserData;
    }

    public void changePassword(String oldPassword, String newPassword,
                               HttpServletRequest httpRequest, HttpServletResponse response) {
        // 取得當前用戶資訊
        String token = jwtUtils.extractJwtFromCookie(httpRequest);
        if (token == null || !jwtUtils.validateToken(token)) {
            throw new BmsException("未登入或 Token 無效");
        }

        String email = jwtUtils.getEmailFromJwtToken(token);
        User user = userService.findByEmail(email);

        if (user == null) {
            throw new BmsException("用戶不存在");
        }

        // **驗證舊密碼**
        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            throw new BmsException("舊密碼不正確");
        }

        // **加密新密碼並更新**
        String encodedNewPassword = passwordEncoder.encode(newPassword);
        user.setPassword(encodedNewPassword);
        userService.save(user); // 只負責數據更新

        // **登出處理**
        logoutUser(httpRequest, response);
    }

    /**
     * 登出處理：清除 Session 和 Cookie
     */
    private void logoutUser(HttpServletRequest httpRequest, HttpServletResponse response) {
        SecurityContextHolder.clearContext(); // 清除 Spring Security Context
        httpRequest.getSession().invalidate(); // 失效 Session

        // 清除 JWT Token
        Cookie cookie = new Cookie("jwtToken", null);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        cookie.setMaxAge(0); // 讓 Cookie 立即失效
        response.addCookie(cookie);
    }


    public List<UserData> listUserDataByUserRoleCustomer() {
        return userService.findAllUserDataByRoleCustomer();
    }
}
