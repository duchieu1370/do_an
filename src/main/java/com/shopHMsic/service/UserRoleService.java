package com.shopHMsic.service;

import com.shopHMsic.dto.UserAdminSearchDto;
import com.shopHMsic.entities.User;
import com.shopHMsic.exception.EntityValidationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface UserRoleService {
    List<User> checkEmailRegister(User entityUser);

    void register(User user) throws EntityValidationException;

    Page<User> getListUser(UserAdminSearchDto dto, Pageable pageable);
}
