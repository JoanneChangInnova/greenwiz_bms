package com.greenwiz.bms.repository;

import com.greenwiz.bms.entity.UserInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * @author Johnny 2025/6/7
 * // 低能需求
 */
public interface UserInfoRepository extends JpaRepository<UserInfo, Long> {
    Optional<UserInfo> findByUserId(Long userId);
}