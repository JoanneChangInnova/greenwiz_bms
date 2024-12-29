package com.greenwiz.bms.service.impl;

import com.greenwiz.bms.controller.data.base.PageReq;
import com.greenwiz.bms.entity.User;
import com.greenwiz.bms.repository.UserRepository;
import com.greenwiz.bms.service.UserService;
import lombok.AccessLevel;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl extends BaseDomainServiceImpl<Long, User> implements UserService {

    @Autowired
    @Getter(value = AccessLevel.PROTECTED)
    private UserRepository jpaRepository;


    @Override
    public Page<User> listUser(PageReq pageReq) {
        Sort.Direction direction = Sort.Direction.fromString(pageReq.getDirection());
        Pageable pageable = PageRequest.of(pageReq.getPage(), pageReq.getSize(), direction, pageReq.getSortBy());
        return jpaRepository.findAll(pageable);
    }
}
