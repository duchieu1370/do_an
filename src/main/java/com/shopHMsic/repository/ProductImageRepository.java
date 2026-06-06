package com.shopHMsic.repository;

import com.shopHMsic.entities.ProductImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductImageRepository extends JpaRepository<ProductImage, Integer> {
    List<ProductImage> findByProductId(Integer productId);

    void deleteAllByProductId(Integer productId);
}
