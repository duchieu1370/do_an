package com.shopHMsic.service.impl;

import com.shopHMsic.dto.UserAdminSearchDto;
import com.shopHMsic.entities.Role;
import com.shopHMsic.entities.User;
import com.shopHMsic.entities.UserRole;
import com.shopHMsic.exception.EntityValidationException;
import com.shopHMsic.repository.RoleRepository;
import com.shopHMsic.repository.UserRepository;
import com.shopHMsic.repository.UserRoleRepository;
import com.shopHMsic.service.UserRoleService;
import jakarta.transaction.Transactional;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class UserRoleServiceImpl implements UserRoleService {
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Autowired
    protected UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    UserRoleRepository userRoleRepository;

    public UserRoleServiceImpl(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    @Override
    public List<User> checkEmailRegister(User entityUser) {
        return userRepository.findByEmailRegister(entityUser.getEmail());
    }

    @Override
    @Transactional
    public void register(User user) throws EntityValidationException {
        if (user.getUsername() == null || user.getUsername().trim().length() < 8) {
            throw new EntityValidationException("Tài khoản phải chứa tối thiểu 8 ký tự!");
        }
        if (user.getPassword() == null || !user.getPassword().matches("^(?=.*[A-Z])(?=.*\\d)(?=.*[!@#$%^&*(),.?\":{}|<>]).{8,}$")) {
            throw new EntityValidationException("Mật khẩu phải chứa tối thiểu 8 ký tự, bao gồm ít nhất 1 chữ hoa, 1 chữ số và 1 ký tự đặc biệt!");
        }
        if (user.getEmail() == null || !user.getEmail().matches("^[a-zA-Z0-9._%+-]+@gmail\\.com$")) {
            throw new EntityValidationException("Địa chỉ email phải đúng định dạng @gmail.com!");
        }
        if (user.getAddress() == null) {
            user.setAddress("");
        }

        List<User> usersMail = userRepository.findByEmailRegister(user.getEmail());
        List<User> usersName = userRepository.findByUserNameRegister(user.getUsername());
        if (!usersMail.isEmpty() || !usersName.isEmpty()) {
            throw new EntityValidationException("Tài khoản hoặc email của bạn đã được đăng ký!");
        }

        user.setPassword(BCrypt.hashpw(user.getPassword(), BCrypt.gensalt(4)));
        user.setCreatedDate(new Date());
        User userNew = userRepository.save(user);

        Role role = roleRepository.findByName("GUEST");
        UserRole userRole = new UserRole();
        userRole.setUserId(userNew.getId());
        userRole.setRoleId(role.getId());
        userRole.setCreatedDate(new Date());
        userRoleRepository.save(userRole);
    }

    @Override
    public Page<User> getListUser(UserAdminSearchDto dto, Pageable pageable) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(" SELECT * ");
        stringBuilder.append(" FROM tbl_users a ");
        stringBuilder.append(" WHERE 1 = 1  ");

        MapSqlParameterSource mapInput = new MapSqlParameterSource();

        if (StringUtils.isNotBlank(dto.getUserName())) {
            stringBuilder.append(" AND a.USERNAME like '%").append(dto.getUserName()).append("%' ");
        }
        if (StringUtils.isNotBlank(dto.getProvince())) {
            stringBuilder.append(" AND UPPER(a.ADDRESS) LIKE UPPER('%").append(dto.getProvince()).append("%') ");
        }
        if (StringUtils.isNotBlank(dto.getWard())) {
            stringBuilder.append(" AND UPPER(a.ADDRESS) LIKE UPPER('%").append(dto.getWard()).append("%') ");
        }

        // Fetch total elements
        StringBuilder countQuery = new StringBuilder();
        countQuery.append(" SELECT COUNT(*) FROM tbl_users a WHERE 1 = 1 ");
        if (StringUtils.isNotBlank(dto.getUserName())) {
            countQuery.append(" AND a.USERNAME like '%").append(dto.getUserName()).append("%' ");
        }
        if (StringUtils.isNotBlank(dto.getProvince())) {
            countQuery.append(" AND UPPER(a.ADDRESS) LIKE UPPER('%").append(dto.getProvince()).append("%') ");
        }
        if (StringUtils.isNotBlank(dto.getWard())) {
            countQuery.append(" AND UPPER(a.ADDRESS) LIKE UPPER('%").append(dto.getWard()).append("%') ");
        }

        Long totalElements = namedParameterJdbcTemplate.queryForObject(countQuery.toString(), mapInput, Long.class);
        if (totalElements == null) {
            totalElements = 0L;
        }

        stringBuilder.append("order by created_date desc OFFSET :page_ ROWS FETCH NEXT :size_ ROWS ONLY ");
        mapInput.addValue("page_", pageable.getOffset());
        mapInput.addValue("size_", pageable.getPageSize());

        List<User> resultList = namedParameterJdbcTemplate.query(stringBuilder.toString(), mapInput, BeanPropertyRowMapper.newInstance(User.class));
        if (CollectionUtils.isNotEmpty(resultList)) {
            return new PageImpl<>(resultList, pageable, totalElements);
        } else {
            return new PageImpl<>(new ArrayList<>(), pageable, 0);
        }
    }
}
