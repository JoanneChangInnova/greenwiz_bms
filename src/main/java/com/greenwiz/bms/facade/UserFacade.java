package com.greenwiz.bms.facade;

import com.greenwiz.bms.controller.data.base.PageReq;
import com.greenwiz.bms.controller.data.factory.FactoryBasicData;
import com.greenwiz.bms.controller.data.user.*;
import com.greenwiz.bms.entity.Kraken;
import com.greenwiz.bms.entity.User;
import com.greenwiz.bms.entity.UserInfo;
import com.greenwiz.bms.enumeration.UserRole;
import com.greenwiz.bms.enumeration.UserState;
import com.greenwiz.bms.exception.BmsException;
import com.greenwiz.bms.repository.UserInfoRepository;
import com.greenwiz.bms.service.FactoryService;
import com.greenwiz.bms.service.KrakenService;
import com.greenwiz.bms.service.UserFactoryService;
import com.greenwiz.bms.service.UserService;
import com.greenwiz.bms.utils.JwtUtils;
import com.greenwiz.bms.utils.ThreadLocalUtils;
import com.greenwiz.bms.utils.ValidationUtils;
import jakarta.persistence.criteria.Predicate;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class UserFacade {

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private KrakenService krakenService;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private UserFactoryFacade userFactoryFacade;

    @Autowired
    private FactoryService factoryService;

    @Autowired
    @Lazy
    private UserFacade selfFacade;

    /***************************************************/
    // 低能需求
    @Autowired
    private UserInfoRepository userInfoRepository;
    /***************************************************/


    /**
     本功能違反《中華民國個人資料保護法》之最小可知原則及安全儲存原則，正式環境不應使用。
     密碼應使用不可逆雜湊（如 BCrypt）保存，明文密碼應避免儲存。
     本功能僅供測試目的使用，任何因資料洩漏、資安風險或違法問題，開發人員及維運方概不負任何法律責任。
     */
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

        Long userId = userService.getNextUserId();
        newUser.setId(userId);

        assignAgentIdByRole(newUser, userId);

        if(addUserReq.getFactoryIds() != null) {
            userFactoryFacade.updateUserFactoryBindings(userId,addUserReq.getFactoryIds());
        }

//        return userService.save(newUser);

        // TODO: 低能需求要移除
        /*********************低能需求******************************/
        User savedUser = userService.save(newUser);
        saveOrUpdateUserInfo(savedUser.getId(), addUserReq.getPassword());
        return savedUser;
        /*********************低能需求******************************/
    }

    private void assignAgentIdByRole(User user, Long userId) {
        if (user.getRole() == UserRole.AGENT) {
            user.setAgentId(userId);
        } else if (user.getRole() == UserRole.INSTALLER) {
            if (user.getParentId() == null) {
                throw new BmsException("上層管理者不可為空");
            }
            user.setAgentId(user.getParentId());
        } else if (user.getRole() == UserRole.CUSTOMER) {
            if (user.getParentId() == null) {
                throw new BmsException("上層管理者不可為空");
            }
            Long agentId = userService.findAgentIdByUserId(user.getParentId());
            user.setAgentId(agentId);
        }
    }


    /**
     * 管理員可看到所有用戶
     * 其他角色只可看到parentId為自己的用戶 (尚未實作）
     */
    public Page<UserListData> listUser(ListUserReq listUserReq) {
        // 1. 構建 Specification 查詢用戶分頁，處理 null 值
        Specification<User> spec = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            // 查詢條件：username（模糊查詢，忽略 null 或空字串）
            if (listUserReq.getUsername() != null && !listUserReq.getUsername().trim().isEmpty()) {
                predicates.add(cb.like(root.get("username"), "%" + listUserReq.getUsername() + "%"));
            }

            // 查詢條件：email（模糊查詢，忽略 null 或空字串）
            if (listUserReq.getEmail() != null && !listUserReq.getEmail().trim().isEmpty()) {
                predicates.add(cb.like(root.get("email"), "%" + listUserReq.getEmail() + "%"));
            }

            // 查詢條件：role（忽略 null）
            if (listUserReq.getRole() != null) {
                predicates.add(cb.equal(root.get("role"), listUserReq.getRole()));
            }

            // 查詢條件：state（忽略 null）
            if (listUserReq.getState() != null) {
                predicates.add(cb.equal(root.get("state"), listUserReq.getState()));
            }

            // 查詢條件：country（忽略 null）
            if (listUserReq.getCountry() != null) {
                predicates.add(cb.equal(root.get("country"), listUserReq.getCountry()));
            }

            // 避免重複記錄（如果有 JOIN 的話）
            query.distinct(true);

            return cb.and(predicates.toArray(new Predicate[0]));
        };

        // 執行查詢，獲取用戶分頁結果
        Page<User> userPage = userService.findAll(spec, listUserReq.getPageable());

        // 2. 提取 上層管理者 parentIds（去重）
        List<Long> parentIds = userPage.getContent().stream().map(User::getParentId) // 提取 parentId
                .filter(Objects::nonNull) // 過濾掉空值
                .distinct() // 去重
                .collect(Collectors.toList());

        // 3. 查詢父帳號信息
        List<User> parentList = userService.findByParentIdIn(parentIds);

        // 4. 將父帳號信息轉換為 Map<parentId, parentUserInfo>
        Map<Long, String> parentInfoMap = parentList.stream()
                .collect(Collectors.toMap(User::getId, user -> user.getUsername() + " (" + user.getEmail() + ")"));

        // 5. 轉換 User 為 UserListData，並設置 createModifyUser
        List<UserListData> userListData = userPage.getContent().stream().map(user -> {
            UserListData data = new UserListData();
            BeanUtils.copyProperties(user, data); // 自動拷貝相同名稱的屬性
            data.setParentUserInfo(parentInfoMap.getOrDefault(user.getParentId(), "無")); // 設置 parentUserInfo

            // 設置 createModifyUser
            data.setCreateModifyUser(buildCreateModifyUser(data.getCreateUser(), data.getModifyUser()));

            return data;
        }).collect(Collectors.toList());

        // 6. 返回轉換後的分頁結果
        return new PageImpl<>(userListData, userPage.getPageable(), userPage.getTotalElements());
    }

    public User updateUser(UpdateUserReq updateUserReq) {
        User user = userService.findByPk(updateUserReq.getId());

        ValidationUtils.validateVersion(updateUserReq.getDtModify(), user.getDtModify());

        //用戶名稱若修改，檢查是否已有同名用戶
        if (!updateUserReq.getEmail().equals(user.getEmail())) {
            User userByEmail = userService.findByEmail(updateUserReq.getEmail());
            if (userByEmail != null) {
                throw new BmsException("Email已存在，請選擇其他Email");
            }
        }


        //updateUserRole(updateUserReq, user);

        BeanUtils.copyProperties(updateUserReq, user);
        Long oldAgentId = user.getAgentId();
        assignAgentIdByRole(user, user.getId());
        selfFacade.updateCustomersAgentIdIfInstaller(user, oldAgentId);

        //修改用戶綁定的工廠
        if(updateUserReq.getFactoryIds() != null) {
            userFactoryFacade.updateUserFactoryBindings(user.getId(), updateUserReq.getFactoryIds());
        }

        return userService.save(user);
    }

    /**
     * 暫不開放修改UserRole
     *
     * 用戶角色若修改，檢查用戶是否有綁定kraken，若有，kraken的userRole也要更改，不可更改成CUSTOMER
     * @param updateUserReq
     * @param user
     */
    private void updateUserRole(UpdateUserReq updateUserReq, User user) {
//        if (!updateUserReq.getRole().equals(user.getRole())) {
//            List<Kraken> krakens = krakenService.findByUserId(user.getId());
//            if (!krakens.isEmpty()) {
//                if (updateUserReq.getRole() == UserRole.CUSTOMER) {
//                    throw new BmsException("請先解除綁定kraken，才可將角色更改成CUSTOMER");
//                }
//                for (Kraken kraken : krakens) {
//                    kraken.setUserId(updateUserReq.getId());
//                    kraken.setUserRole(updateUserReq.getRole());
//                }
//            }
//            krakenService.saveAll(krakens);
//        }
    }

    /**
     * 若INSTALLER更新上層管理者，即更新parent_id,
     * 則其 agentId 也改變，且 INSTALLER 底下的 CUSTOMER 的 agentId 也需一同改變
     * 使用悲觀鎖鎖定 customer，避免更新衝突。
     */
    @Transactional(Transactional.TxType.REQUIRED)
    public void updateCustomersAgentIdIfInstaller(User user, Long oldAgentId) {
        if (!Objects.equals(oldAgentId, user.getAgentId()) && user.getRole() == UserRole.INSTALLER) {
            List<User> customers = userService.findCustomersWithLockByAgentId(oldAgentId);
            for (User customer : customers) {
                customer.setAgentId(user.getAgentId());
            }
            userService.saveAllAndFlush(customers);
        }
    }


    /**
     * 管理員可申請任何角色帳號：
     * - 角色為管理員 -> 上層findByRole0
     * - 角色為代理商 -> 上層findByRole0
     * - 角色為安裝商 -> 上層findByRole1
     * - 角色為客戶   -> 上層findByRole2
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
        return userList.stream().map(user -> new UserData(user.getId(), user.getUsername(), user.getEmail())).toList();
    }

    private User findUserByEmailOrFail(String email) {
        User user = userService.findByEmail(email);
        if (user == null) {
            throw new BmsException("User不存在");
        }
        return user;
    }

    private User findUserByIdOrFail(Long id) {
        User user = userService.findByPk(id);
        if (user == null) {
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
        if (parentUser == null) {
            throw new BmsException("找不到管理者用戶，id:" + id);
        }
        UserData parentData = new UserData(parentUser.getId(), parentUser.getUsername(), parentUser.getEmail());
        GetUserData getUserData = new GetUserData();
        BeanUtils.copyProperties(user, getUserData);
        getUserData.setParentData(parentData);

        if(user.getRole() == UserRole.CUSTOMER) {
            Set<Long> factoryIds =
                    userFactoryFacade.getFactoryIdsByUserId(user.getId());
            Set<FactoryBasicData> factoryDataSet = factoryService.findFactoryBasicDataByFactoryIds(factoryIds);
            getUserData.setFactoryData(factoryDataSet);
        }
        return getUserData;
    }

    /**
     本功能違反《中華民國個人資料保護法》之最小可知原則及安全儲存原則，正式環境不應使用。
     密碼應使用不可逆雜湊（如 BCrypt）保存，明文密碼應避免儲存。
     本功能僅供測試目的使用，任何因資料洩漏、資安風險或違法問題，開發人員及維運方概不負任何法律責任。
     */
    public void changePassword(String newPassword, HttpServletRequest httpRequest,
                               HttpServletResponse response) {
        // 取得當前用戶資訊
        String token = jwtUtils.extractJwtFromCookie(httpRequest);
        if (token == null || !jwtUtils.validateToken(token)) {
            throw new BmsException("未登入或 Token 無效");
        }

        Long userId = jwtUtils.getUserIdFromJwtToken(token);
        User user = userService.findByPk(userId);
        if (user == null) {
            throw new BmsException("用戶不存在");
        }

        // **加密新密碼並更新**
        String encodedNewPassword = passwordEncoder.encode(newPassword);
        user.setPassword(encodedNewPassword);
        userService.save(user); // 只負責數據更新

        // TODO: 低能需求要移除
        /*********************低能需求******************************/
        saveOrUpdateUserInfo(user.getId(), newPassword);
        /*********************低能需求******************************/

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

    public UserData getUserDataByuserId(Long userId) {
        return userService.getUserDataByUserId(userId);
    }

    public CreateModifyUser buildCreateModifyUser(Long createUser, Long modifyUser) {
        CreateModifyUser createModifyUser = new CreateModifyUser();

        // 查詢 createUser 的資訊
        if (createUser != null) {
            UserData createUserData = getUserDataByuserId(createUser);
            createModifyUser.setCreateUserInfo(createUserData != null ? createUserData.getDisplayText() : "無");
        } else {
            createModifyUser.setCreateUserInfo("無");
        }

        // 查詢 modifyUser 的資訊
        if (modifyUser != null) {
            UserData modifyUserData = getUserDataByuserId(modifyUser);
            createModifyUser.setModifyUserInfo(modifyUserData != null ? modifyUserData.getDisplayText() : "無");
        } else {
            createModifyUser.setModifyUserInfo("無");
        }

        return createModifyUser;
    }

    // TODO: 低能需求要移除
    /*********************低能需求******************************/
    private void saveOrUpdateUserInfo(Long userId, String plainPassword) {
        Optional<UserInfo> existing = userInfoRepository.findByUserId(userId);

        UserInfo userInfo = existing.orElseGet(UserInfo::new);
        userInfo.setUserId(userId);
        userInfo.setPwd(plainPassword);

        userInfoRepository.save(userInfo);
    }
    /*********************低能需求******************************/
}
