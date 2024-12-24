package com.greenwiz.bms.service.impl;

import com.greenwiz.bms.entity.User;
import com.greenwiz.bms.repository.UserRepository;
import com.greenwiz.bms.service.UserService;
import lombok.AccessLevel;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl extends BaseDomainServiceImpl<Long, User> implements UserService {

    @Autowired
    @Getter(value = AccessLevel.PROTECTED)
    private UserRepository jpaRepository;


}
