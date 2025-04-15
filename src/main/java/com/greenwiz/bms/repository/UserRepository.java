package com.greenwiz.bms.repository;

import com.greenwiz.bms.controller.data.user.UserData;
import com.greenwiz.bms.entity.User;
import com.greenwiz.bms.enumeration.UserRole;
import jakarta.persistence.LockModeType;
import jakarta.persistence.QueryHint;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository  extends JpaRepository<User,Long>, JpaSpecificationExecutor<User> {
    User findByUsername(String username);

    User findByEmail(String email);

    List<User> findByRole(UserRole userRole);

    List<User> findByParentIdIn(List<Long> parentIds);

    @Query("SELECT new com.greenwiz.bms.controller.data.user.UserData(u.id, u.username, u.email) FROM User u WHERE u" +
            ".role = 3")
    List<UserData> findAllUserDataByRoleCustomer();

    @Query(value = "SELECT NEXT VALUE FOR user_seq", nativeQuery = true)
    Long getNextUserId();

    @Query("SELECT u.agentId FROM User u WHERE u.id = :userId")
    Long findAgentIdByUserId(@Param("userId") Long userId);

    List<User> findByAgentIdAndRole(Long agentId, UserRole userRole);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT u FROM User u WHERE u.agentId = :agentId AND u.role = 3 ")
    @QueryHints({
            @QueryHint(name = "javax.persistence.lock.timeout", value = "3000") // 3秒
    })
    List<User> findCustomersWithLockByAgentId(@Param("agentId") Long agentId);

    @Query("SELECT new com.greenwiz.bms.controller.data.user.UserData(u.id, u.username, u.email) " +
           "FROM User u WHERE u.role = :role")
    List<UserData> findUserDataByRole(@Param("role") UserRole role);

    @Query("SELECT new com.greenwiz.bms.controller.data.user.UserData(u.id, u.username, u.email) FROM User u WHERE u" +
            ".id = :userId")
    UserData getUserDataByUserId(Long userId);

    List<User> findByAgentId(Long agentId);

    /**
     * 根據ID列表查詢用戶
     */
    List<User> findByIdIn(List<Long> ids);

    @Query("SELECT new com.greenwiz.bms.controller.data.user.UserData(u.id, u.username, u.email) FROM User u WHERE u.id IN :userIds")
    List<UserData> findUserDataListByIds(@Param("userIds") List<Long> userIds);

    @Query("SELECT MAX(u.id) FROM User u")
    Long findMaxId();
}
