package com.shopHMsic.service.impl;

import com.shopHMsic.dto.ContactSearchModel;
import com.shopHMsic.entities.Contact;
import com.shopHMsic.exception.EntityValidationException;
import com.shopHMsic.repository.ContactRepository;
import com.shopHMsic.service.ContactService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.List;

@Service
public class ContactServiceImpl implements ContactService {

    @Autowired
    private ContactRepository contactRepository;

    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Page<Contact> getListContact(ContactSearchModel dto, Pageable pageable) {
        StringBuilder sql = new StringBuilder("SELECT * FROM tbl_contact WHERE 1=1");
        MapSqlParameterSource mapInput = new MapSqlParameterSource();

        if (dto != null && org.apache.commons.lang3.StringUtils.isNotBlank(dto.keyword)) {
            sql.append(" AND (name LIKE :keyword OR email LIKE :keyword OR massage LIKE :keyword)");
            mapInput.addValue("keyword", "%" + dto.keyword + "%");
        }

        // Fetch total count
        String countSql = "SELECT COUNT(*) FROM (" + sql.toString() + ")";
        Long total = namedParameterJdbcTemplate.queryForObject(countSql, mapInput, Long.class);
        if (total == null) {
            total = 0L;
        }

        sql.append(" ORDER BY id DESC OFFSET :offset_ ROWS FETCH NEXT :size_ ROWS ONLY");
        mapInput.addValue("offset_", pageable.getOffset());
        mapInput.addValue("size_", pageable.getPageSize());

        List<Contact> list = namedParameterJdbcTemplate.query(sql.toString(), mapInput, BeanPropertyRowMapper.newInstance(Contact.class));
        if (org.apache.commons.collections.CollectionUtils.isNotEmpty(list)) {
            return new PageImpl<>(list, pageable, total);
        } else {
            return new PageImpl<>(new ArrayList<>(), pageable, 0);
        }
    }

    @Override
    public Contact getById(int id) {
        return contactRepository.findById(id).orElse(null);
    }

    @Override
    @Transactional
    public void deleteContact(int id) throws Exception {
        Contact contact = contactRepository.findById(id).orElse(null);
        if (contact == null) {
            throw new EntityValidationException("Không tìm thấy thư liên hệ");
        }
        contactRepository.delete(contact);
    }

    @Override
    @Transactional
    public void updateContactStatus(int id, boolean status) throws Exception {
        Contact contact = contactRepository.findById(id).orElse(null);
        if (contact == null) {
            throw new EntityValidationException("Không tìm thấy thư liên hệ");
        }
        contact.setStatus(status);
        contact.setUpdatedDate(new java.util.Date());
        contactRepository.save(contact);
    }

    @Override
    @Transactional
    public Contact saveOrUpdate(Contact contact) {
        if (contact.getId() == null || contact.getId() <= 0) {
            contact.setCreatedDate(new java.util.Date());
            entityManager.persist(contact);
            return contact;
        } else {
            contact.setUpdatedDate(new java.util.Date());
            return entityManager.merge(contact);
        }
    }
}
