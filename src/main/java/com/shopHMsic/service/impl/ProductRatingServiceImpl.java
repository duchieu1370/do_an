package com.shopHMsic.service.impl;

import com.shopHMsic.entities.ProductRating;
import com.shopHMsic.exception.EntityValidationException;
import com.shopHMsic.repository.ProductRatingRepository;
import com.shopHMsic.service.ProductRatingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class ProductRatingServiceImpl implements ProductRatingService {

    @Autowired
    private ProductRatingRepository ratingRepository;

    @Override
    public List<ProductRating> getRatingsByProductId(int productId) {
        return ratingRepository.findByProductId(productId);
    }

    @Override
    @Transactional
    public ProductRating saveOrUpdateRating(ProductRating rating) throws Exception {
        if (rating.getRating() == null || rating.getRating() < 0 || rating.getRating() > 5) {
            throw new EntityValidationException("Số sao đánh giá phải từ 0 đến 5 sao!");
        }
        if (rating.getProduct() == null || rating.getProduct().getId() == null) {
            throw new EntityValidationException("Sản phẩm đánh giá không hợp lệ!");
        }
        if (rating.getUser() == null || rating.getUser().getId() == null) {
            throw new EntityValidationException("Người dùng đánh giá không hợp lệ!");
        }

        // Kiểm tra xem đã tồn tại đánh giá cũ chưa
        Optional<ProductRating> oldRatingOpt = ratingRepository.findByProductIdAndUserId(
                rating.getProduct().getId(), rating.getUser().getId()
        );

        if (oldRatingOpt.isPresent()) {
            // Sửa đổi trên chính đánh giá cũ
            ProductRating oldRating = oldRatingOpt.get();
            oldRating.setRating(rating.getRating());
            oldRating.setComment(rating.getComment());
            oldRating.setUpdatedDate(new Date());
            return ratingRepository.save(oldRating);
        } else {
            // Tạo mới đánh giá
            rating.setCreatedDate(new Date());
            rating.setStatus(true); // Trạng thái hiển thị mặc định là true
            return ratingRepository.save(rating);
        }
    }

    @Override
    public boolean hasUserRatedProduct(int productId, int userId) {
        return ratingRepository.findByProductIdAndUserId(productId, userId).isPresent();
    }

    @Override
    public ProductRating getUserRatingForProduct(int productId, int userId) {
        return ratingRepository.findByProductIdAndUserId(productId, userId).orElse(null);
    }
}
