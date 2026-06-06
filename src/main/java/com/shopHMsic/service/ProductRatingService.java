package com.shopHMsic.service;

import com.shopHMsic.entities.ProductRating;
import java.util.List;

public interface ProductRatingService {
    List<ProductRating> getRatingsByProductId(int productId);
    
    ProductRating saveOrUpdateRating(ProductRating rating) throws Exception;
    
    boolean hasUserRatedProduct(int productId, int userId);
    
    ProductRating getUserRatingForProduct(int productId, int userId);
}
