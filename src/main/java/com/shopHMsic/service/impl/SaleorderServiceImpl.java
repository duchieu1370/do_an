package com.shopHMsic.service.impl;

import com.shopHMsic.dto.OrderSearchModel;
import com.shopHMsic.entities.Saleorder;
import com.shopHMsic.entities.SaleorderProducts;
import com.shopHMsic.exception.EntityValidationException;
import com.shopHMsic.repository.OrderRepository;
import com.shopHMsic.service.SaleorderService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class SaleorderServiceImpl implements SaleorderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Page<Saleorder> getListOrder(OrderSearchModel dto, Pageable pageable) {
        StringBuilder sql = new StringBuilder("SELECT * FROM tbl_saleorder WHERE 1=1");
        MapSqlParameterSource mapInput = new MapSqlParameterSource();

        if (dto != null && org.apache.commons.lang3.StringUtils.isNotBlank(dto.keyword)) {
            sql.append(" AND (customer_name LIKE :keyword OR customer_email LIKE :keyword OR customer_phone LIKE :keyword OR customer_address LIKE :keyword OR code LIKE :keyword)");
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

        List<Saleorder> list = namedParameterJdbcTemplate.query(sql.toString(), mapInput, BeanPropertyRowMapper.newInstance(Saleorder.class));
        if (org.apache.commons.collections.CollectionUtils.isNotEmpty(list)) {
            for (Saleorder order : list) {
                try {
                    List<SaleorderProducts> products = entityManager.createQuery(
                                    "SELECT op FROM SaleorderProducts op WHERE op.saleOrder.id = :orderId",
                                    SaleorderProducts.class)
                            .setParameter("orderId", order.getId())
                            .getResultList();
                    order.setSaleOrderProducts(new java.util.HashSet<>(products));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return new PageImpl<>(list, pageable, total);
        } else {
            return new PageImpl<>(new ArrayList<>(), pageable, 0);
        }
    }

    @Override
    public Saleorder getById(int id) {
        return orderRepository.findById(id).orElse(null);
    }

    @Override
    @Transactional
    public void updateOrderStatus(int id, Integer orderStatus, String reason) throws Exception {
        Saleorder order = orderRepository.findById(id).orElse(null);
        if (order == null) {
            throw new EntityValidationException("Không tìm thấy đơn hàng");
        }
        order.setOrderStatus(orderStatus);
        order.setReason(reason);
        order.setStatus(orderStatus);
        order.setUpdatedDate(new java.util.Date());
        orderRepository.save(order);
    }

    @Override
    @Transactional
    public void deleteOrder(int id) throws Exception {
        Saleorder order = orderRepository.findById(id).orElse(null);
        if (order == null) {
            throw new EntityValidationException("Không tìm thấy đơn hàng");
        }
        orderRepository.delete(order);
    }

    @Override
    @Transactional
    public Saleorder saveOrUpdate(Saleorder order) {
        if (order.getId() == null || order.getId() <= 0) {
            order.setCreatedDate(new java.util.Date());
            entityManager.persist(order);
            return order;
        } else {
            order.setUpdatedDate(new java.util.Date());
            return entityManager.merge(order);
        }
    }

}
