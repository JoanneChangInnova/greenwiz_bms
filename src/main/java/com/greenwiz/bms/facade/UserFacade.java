package com.greenwiz.bms.facade;

import com.greenwiz.bms.controller.data.base.PageReq;
import com.greenwiz.bms.controller.data.user.AddUserReq;
import com.greenwiz.bms.controller.data.user.UpdateUserReq;
import com.greenwiz.bms.entity.User;
import com.greenwiz.bms.enumeration.UserState;
import com.greenwiz.bms.exception.BmsException;
import com.greenwiz.bms.service.UserService;
import com.greenwiz.bms.utils.ValidationUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserFacade {

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

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

    public Page<User> listUser(PageReq pageReq) {
        return userService.listUser(pageReq);
    }

    public User updateUser(UpdateUserReq updateUserReq) {
        User user = userService.findByPk(updateUserReq.getId());

        ValidationUtils.validateVersion(updateUserReq.getDtModify(),user.getDtModify());

        //用戶名稱若修改，檢查是否已有同名用戶
        if(!updateUserReq.getUsername().equals(user.getEmail())){
            User userByEmail = userService.findByEmail(updateUserReq.getEmail());
            if(userByEmail != null){
                throw new BmsException("Email已存在，請選擇其他Email");
            }
        }

        BeanUtils.copyProperties(updateUserReq, user);
        return userService.save(user);
    }
}
