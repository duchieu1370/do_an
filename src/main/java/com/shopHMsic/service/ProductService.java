package com.shopHMsic.service;

import com.shopHMsic.dto.ProductSearchModel;
import com.shopHMsic.entities.Product;
import com.shopHMsic.exception.EntityValidationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

public interface ProductService {
    Page<Product> getListProduct(ProductSearchModel dto, Pageable pageable);
    
    Product getById(int id);

    Product getBySeo(String seo);
    
    void add(Product p, MultipartFile productAvatar, MultipartFile[] productPictures) throws Exception;
    
    void update(Product p, MultipartFile productAvatar, MultipartFile[] productPictures) throws Exception;
    
    void deleteById(int id) throws EntityValidationException;
    
    void updateProductStatus(int id, boolean status) throws Exception;
}
