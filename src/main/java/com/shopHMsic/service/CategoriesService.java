package com.shopHMsic.service;

import com.shopHMsic.dto.CategoriesDto;
import com.shopHMsic.dto.CategorySearchModel;
import com.shopHMsic.entities.Categories;
import com.shopHMsic.exception.EntityValidationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CategoriesService {
    void createCategories(CategoriesDto dto) throws EntityValidationException;

    void updateCategory(int id, CategoriesDto dto) throws EntityValidationException;

    void deleteCategory(int id) throws EntityValidationException;

    Page<Categories> getListCategory(CategorySearchModel dto, Pageable pageable);

    java.util.List<Categories> getAllCategories();

    void updateCategoryStatus(int id, boolean status) throws EntityValidationException;
}
