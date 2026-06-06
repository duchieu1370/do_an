package com.shopHMsic.service.impl;

import com.github.slugify.Slugify;
import com.shopHMsic.dto.CategoriesDto;
import com.shopHMsic.dto.CategorySearchModel;
import com.shopHMsic.entities.Categories;
import com.shopHMsic.exception.EntityValidationException;
import com.shopHMsic.repository.CategoriesRepository;
import com.shopHMsic.service.CategoriesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class CategoriesServiceImpl implements CategoriesService {

    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Autowired
    private CategoriesRepository categoriesRepository;

    @Override
    public void createCategories(CategoriesDto dto) throws EntityValidationException {
        if (dto == null) {
            throw new EntityValidationException("Lỗi hệ thống");
        }
        if (dto.getName() == null || dto.getName().trim().isEmpty()) {
            throw new EntityValidationException("Tên danh mục không được để trống!");
        }
        Categories category = new Categories();
        category.setCreatedDate(new Date());
        category.setName(dto.getName().trim());
        category.setDescription(dto.getDescription() != null ? dto.getDescription().trim() : "");

        if (dto.getSeo() == null || dto.getSeo().trim().isEmpty()) {
            category.setSeo(new Slugify().slugify(dto.getName().trim()));
        } else {
            category.setSeo(new Slugify().slugify(dto.getSeo().trim()));
        }

        categoriesRepository.save(category);
    }

    @Override
    public void updateCategory(int id, CategoriesDto dto) throws EntityValidationException {
        if (dto == null) {
            throw new EntityValidationException("Lỗi hệ thống");
        }
        Categories category = categoriesRepository.findById(id).orElse(null);
        if (category == null) {
            throw new EntityValidationException("Không tìm thấy danh mục để cập nhật!");
        }
        if (dto.getName() == null || dto.getName().trim().isEmpty()) {
            throw new EntityValidationException("Tên danh mục không được để trống!");
        }
        category.setName(dto.getName().trim());
        category.setDescription(dto.getDescription() != null ? dto.getDescription().trim() : "");
        category.setUpdatedDate(new Date());

        if (dto.getSeo() == null || dto.getSeo().trim().isEmpty()) {
            category.setSeo(new Slugify().slugify(dto.getName().trim()));
        } else {
            category.setSeo(new Slugify().slugify(dto.getSeo().trim()));
        }

        categoriesRepository.save(category);
    }

    @Override
    public void deleteCategory(int id) throws EntityValidationException {
        Categories category = categoriesRepository.findById(id).orElse(null);
        if (category == null) {
            throw new EntityValidationException("Không tìm thấy danh mục để xóa!");
        }
        try {
            categoriesRepository.delete(category);
        } catch (Exception e) {
            throw new EntityValidationException("Không thể xóa danh mục này do đang có sản phẩm thuộc danh mục!");
        }
    }

    public Page<Categories> getListCategory(CategorySearchModel dto, Pageable pageable) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(" SELECT ");
        stringBuilder.append("   a.id, ");
        stringBuilder.append("   a.name, ");
        stringBuilder.append("   a.description, ");
        stringBuilder.append("   a.seo, ");
        stringBuilder.append("   a.status, ");
        stringBuilder.append("   a.created_date, ");
        stringBuilder.append("   a.updated_date, ");
        stringBuilder.append("   a.created_by, ");
        stringBuilder.append("   a.updated_by, ");
        stringBuilder.append("   a.parent_id ");
        stringBuilder.append(" FROM tbl_category a ");
        stringBuilder.append(" WHERE 1 = 1 ");

        MapSqlParameterSource mapInput = new MapSqlParameterSource();

        if (dto != null) {
            if (org.apache.commons.lang3.StringUtils.isNotBlank(dto.getKeyword())) {
                stringBuilder.append(" AND (a.name LIKE :keyword OR a.description LIKE :keyword) ");
                mapInput.addValue("keyword", "%" + dto.getKeyword() + "%");
            }
        }

        // Fetch total elements
        StringBuilder countQuery = new StringBuilder();
        countQuery.append(" SELECT COUNT(*) FROM tbl_category a WHERE 1 = 1 ");
        if (dto != null) {
            if (org.apache.commons.lang3.StringUtils.isNotBlank(dto.getKeyword())) {
                countQuery.append(" AND (a.name LIKE :keyword OR a.description LIKE :keyword) ");
            }
        }

        Long totalElements = namedParameterJdbcTemplate.queryForObject(countQuery.toString(), mapInput, Long.class);
        if (totalElements == null) {
            totalElements = 0L;
        }

        stringBuilder.append(" ORDER BY a.created_date DESC OFFSET :page_ ROWS FETCH NEXT :size_ ROWS ONLY ");
        mapInput.addValue("page_", pageable.getOffset());
        mapInput.addValue("size_", pageable.getPageSize());

        List<Categories> resultList = namedParameterJdbcTemplate.query(stringBuilder.toString(), mapInput, BeanPropertyRowMapper.newInstance(Categories.class));
        if (org.apache.commons.collections.CollectionUtils.isNotEmpty(resultList)) {
            return new PageImpl<>(resultList, pageable, totalElements);
        } else {
            return new PageImpl<>(new ArrayList<>(), pageable, 0);
        }
    }

    @Override
    public List<Categories> getAllCategories() {
        return categoriesRepository.findAll();
    }

    @Override
    public void updateCategoryStatus(int id, boolean status) throws EntityValidationException {
        Categories category = categoriesRepository.findById(id).orElse(null);
        if (category == null) {
            throw new EntityValidationException("Không tìm thấy danh mục để cập nhật trạng thái!");
        }
        category.setStatus(status);
        category.setUpdatedDate(new Date());
        categoriesRepository.save(category);
    }
}
