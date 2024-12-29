package com.greenwiz.bms.service;

import com.greenwiz.bms.controller.data.base.PageReq;
import com.greenwiz.bms.entity.User;
import org.springframework.data.domain.Page;

public interface UserService extends BaseDomainService<Long, User> {
    Page<User> listUser(PageReq pageReq);
}
