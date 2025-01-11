package com.greenwiz.bms.service.impl;

import com.greenwiz.bms.controller.data.base.PageReq;
import com.greenwiz.bms.controller.data.user.AddUserReq;
import com.greenwiz.bms.entity.User;
import com.greenwiz.bms.repository.UserRepository;
import com.greenwiz.bms.service.UserService;
import lombok.AccessLevel;
import lombok.Getter;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl extends BaseDomainServiceImpl<Long, User> implements UserService {

    @Autowired
    @Getter(value = AccessLevel.PROTECTED)
    private UserRepository jpaRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public Page<User> listUser(PageReq pageReq) {
        Sort.Direction direction = Sort.Direction.fromString(pageReq.getDirection());
        Pageable pageable = PageRequest.of(pageReq.getPage(), pageReq.getSize(), direction, pageReq.getSortBy());
        return jpaRepository.findAll(pageable);
    }

    @Override
    public User findByUsername(String username) {
        return jpaRepository.findByUsername(username).orElse(null);
    }

    @Override
    public boolean validatePassword(String rawPassword, String encodedPassword) {
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }

    @Override
    public String getRoleName(Integer roleCode) {
        switch (roleCode) {
            case 0:
                return "ADMIN";
            case 1:
                return "AGENT";
            case 2:
                return "INSTALLER";
            case 3:
                return "CUSTOMER";
            default:
                throw new IllegalArgumentException("未知的角色代碼：" + roleCode);
        }
    }

    @Override
    public User addUser(AddUserReq addUserReq) {
        // 1. 檢查角色代碼是否合法
        if (addUserReq.getRole() == null || addUserReq.getRole() < 0 || addUserReq.getRole() > 3) {
            throw new IllegalArgumentException("角色代碼不合法，應為 0 到 3 之間");
        }

        // 2. 檢查用戶名是否已存在
        if (jpaRepository.findByUsername(addUserReq.getUsername()).isPresent()) {
            throw new IllegalArgumentException("用戶名已存在，請選擇其他用戶名");
        }

        // 3. 創建 User 實體並複製屬性
        User user = new User();
        BeanUtils.copyProperties(addUserReq, user);

        // 4. 密碼加密
        String encodedPassword = passwordEncoder.encode(addUserReq.getPassword());
        user.setPassword(encodedPassword);

        // 5. 設置默認值（如果未提供）
        if (user.getState() == null) {
            user.setState(1); // 默認狀態：開通
        }
        if (user.getMaxDevice() == null) {
            user.setMaxDevice(20); // 默認最大設備數量
        }

        // 6. 保存用戶
        return jpaRepository.save(user);
    }

    @Override
    public String encodePassword(String rawPassword) {
        return passwordEncoder.encode(rawPassword);
    }


}
