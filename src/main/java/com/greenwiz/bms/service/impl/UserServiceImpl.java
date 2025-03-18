package com.greenwiz.bms.service.impl;

import com.greenwiz.bms.controller.data.base.PageReq;
import com.greenwiz.bms.entity.User;
import com.greenwiz.bms.enumeration.UserRole;
import com.greenwiz.bms.exception.BmsException;
import com.greenwiz.bms.repository.UserRepository;
import com.greenwiz.bms.service.UserService;
import lombok.AccessLevel;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

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
        return jpaRepository.findByUsername(username);
    }

    @Override
    public User findByEmail(String email) {
        return jpaRepository.findByEmail(email);
    }

    @Override
    public boolean validatePassword(String rawPassword, String encodedPassword) {
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }

    @Override
    public String encodePassword(String rawPassword) {
        return passwordEncoder.encode(rawPassword);
    }

    @Override
    public List<User> findByUserRole(UserRole userRole) {
        return jpaRepository.findByRole(userRole);
    }

    @Override
    public List<User> findByParentIdIn(List<Long> parentIds) {
        return jpaRepository.findByParentIdIn(parentIds);
    }

    @Override
    public void updatePassword(Long userId, String encodedNewPassword) {
        User user = jpaRepository.findById(userId)
                .orElseThrow(() -> new BmsException("用戶不存在"));

        user.setPassword(encodedNewPassword);
        jpaRepository.saveAndFlush(user);
    }

}
