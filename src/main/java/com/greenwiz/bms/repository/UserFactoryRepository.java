package com.greenwiz.bms.repository;

import com.greenwiz.bms.entity.UserFactory;
import com.greenwiz.bms.controller.data.user.UserData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserFactoryRepository extends JpaRepository<UserFactory, Long> {
    /**
     * 根據工廠ID列表查詢用戶工廠關聯記錄
     */
    List<UserFactory> findByFactoryIdIn(List<Long> factoryIds);

    /**
     * 根據工廠ID查詢用戶資料
     * @param factoryId 工廠ID
     * @return 用戶資料列表
     */
    @Query("SELECT new com.greenwiz.bms.controller.data.user.UserData(u.id, u.username, u.email) " +
           "FROM UserFactory uf JOIN User u ON u.id = uf.userId " +
           "WHERE uf.factoryId = :factoryId")
    List<UserData> findUserDataByFactoryId(@Param("factoryId") Long factoryId);

    List<UserFactory> findByUserId(Long userId);
}
