package com.greenwiz.bms.repository;

import com.greenwiz.bms.controller.data.user.UserData;
import com.greenwiz.bms.entity.User;
import com.greenwiz.bms.enumeration.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository  extends JpaRepository<User,Long> {
    User findByUsername(String username);

    User findByEmail(String email);

    List<User> findByRole(UserRole userRole);

    List<User> findByParentIdIn(List<Long> parentIds);

    @Query("SELECT new com.greenwiz.bms.controller.data.user.UserData(u.id, u.email, u.username) FROM User u WHERE u" +
            ".role = 3")
    List<UserData> findAllUserDataByRoleCustomer();
}
