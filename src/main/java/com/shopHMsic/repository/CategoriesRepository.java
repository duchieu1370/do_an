package com.shopHMsic.repository;

import com.shopHMsic.entities.Categories;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoriesRepository extends JpaRepository<Categories, Integer> {
}
