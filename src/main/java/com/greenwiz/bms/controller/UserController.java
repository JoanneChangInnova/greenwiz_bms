package com.greenwiz.bms.controller;

import com.greenwiz.bms.controller.data.base.LayuiTableResp;
import com.greenwiz.bms.controller.data.base.PageReq;
import com.greenwiz.bms.controller.data.user.*;
import com.greenwiz.bms.entity.User;
import com.greenwiz.bms.enumeration.UserRole;
import com.greenwiz.bms.facade.UserFacade;
import com.greenwiz.bms.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/v1/user")
public class UserController {

    @Autowired
    private UserFacade userFacade;

    @Autowired
    private UserService userService;

    @PostMapping("/listParentInfo")
    public List<UserData> listParentInfo(@RequestBody UserRoleRequest request) {
        return userFacade.listParentInfo(request.getUserRole());
    }

    @GetMapping("/listAllParentInfo")
    public Map<String, List<UserData>> listAllParentInfo() {
        Map<String, List<UserData>> allParentInfo = new HashMap<>();

        // 使用 UserRole 枚舉遍歷所有角色
        for (UserRole role : UserRole.values()) {
            List<UserData> parentInfo = userFacade.listParentInfo(role);
            allParentInfo.put(String.valueOf(role.getValue()), parentInfo);
        }

        return allParentInfo;
    }

    @Transactional(Transactional.TxType.REQUIRED)
    @PostMapping("/add")
    public ResponseEntity<?> addUser(@Valid @RequestBody AddUserReq addUserReq) {
        User addedUser = userFacade.addUser(addUserReq);
        return ResponseEntity.status(HttpStatus.CREATED).body(addedUser);
    }

    @PostMapping("/list")
    public LayuiTableResp<UserListData> listUser(@RequestBody PageReq pageReq) {
        Page<UserListData> users = userFacade.listUser(pageReq);
        return LayuiTableResp.success(users.getTotalElements(), users.getContent());
    }


    @Transactional(Transactional.TxType.REQUIRED)
    @PostMapping("/update")
    public ResponseEntity<String> updateUser(@RequestBody @Valid UpdateUserReq updateUserReq) {
        userFacade.updateUser(updateUserReq);
        return ResponseEntity.ok("用戶資料更新成功");
    }

    @GetMapping("/{id}")
    public ResponseEntity<GetUserData> getUserById(@PathVariable Long id) {
        GetUserData getUserData = userFacade.getUserById(id);
        return ResponseEntity.ok(getUserData);
    }

    @PostMapping("/change-password")
    public ResponseEntity<?> changePassword(@RequestBody ChangePasswordRequest request,
                                            HttpServletRequest httpRequest,
                                            HttpServletResponse response) {
        // 透過 UserFacade 處理所有密碼變更邏輯
        userFacade.changePassword(request.getOldPassword(), request.getNewPassword(), httpRequest, response);

        return ResponseEntity.ok("密碼修改成功，請重新登入");
    }

    /**
     * 列出所有客戶角色的使用者 id, email 和 name
     */
    @GetMapping("/listAllCustomers")
    public List<UserData> listUserDataByUserRoleCustomer() {
        List<UserData> userDatas = userFacade.listUserDataByUserRoleCustomer();
        return userDatas;
    }
}
