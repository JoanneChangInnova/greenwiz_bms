package com.greenwiz.bms.repository;

import com.greenwiz.bms.entity.UserFactory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserFactoryRepository extends JpaRepository<UserFactory, Long> {
}
