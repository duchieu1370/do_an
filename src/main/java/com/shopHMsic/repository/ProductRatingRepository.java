package com.shopHMsic.repository;

import com.shopHMsic.entities.ProductRating;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRatingRepository extends JpaRepository<ProductRating, Integer> {
    List<ProductRating> findByProductId(int productId);
    Optional<ProductRating> findByProductIdAndUserId(int productId, int userId);
}
