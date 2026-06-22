package com.shopHMsic.service.impl;

import com.github.slugify.Slugify;
import com.shopHMsic.dto.ProductSearchModel;
import com.shopHMsic.entities.Product;
import com.shopHMsic.entities.ProductImage;
import com.shopHMsic.entities.SaleorderProducts;
import com.shopHMsic.exception.EntityValidationException;
import com.shopHMsic.repository.OrderProductRepository;
import com.shopHMsic.repository.ProductImageRepository;
import com.shopHMsic.repository.ProductRepository;
import com.shopHMsic.service.ProductService;
import com.shopHMsic.service.S3Service;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Autowired
    private S3Service s3Service;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductImageRepository productImageRepository;

    @Autowired
    private OrderProductRepository orderProductRepository;

    private boolean isEmptyUploadFile(MultipartFile[] images) {
        if (images == null || images.length <= 0)
            return true;

        if (images.length == 1 && images[0].getOriginalFilename().isEmpty())
            return true;

        return false;
    }

    private boolean isEmptyUploadFile(MultipartFile image) {
        return image == null || image.getOriginalFilename().isEmpty();
    }

    @Override
    public Page<Product> getListProduct(ProductSearchModel dto, Pageable pageable) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(" SELECT ");
        stringBuilder.append("   a.id, ");
        stringBuilder.append("   a.title, ");
        stringBuilder.append("   a.price, ");
        stringBuilder.append("   a.price_sale, ");
        stringBuilder.append("   a.short_description AS shortDes, ");
        stringBuilder.append("   a.detail_description AS details, ");
        stringBuilder.append("   a.avatar, ");
        stringBuilder.append("   a.seo, ");
        stringBuilder.append("   a.status, ");
        stringBuilder.append("   a.created_date, ");
        stringBuilder.append("   a.updated_date, ");
        stringBuilder.append("   a.created_by, ");
        stringBuilder.append("   a.updated_by ");
        stringBuilder.append(" FROM tbl_products a ");
        stringBuilder.append(" WHERE 1 = 1 ");

        MapSqlParameterSource mapInput = new MapSqlParameterSource();

        if (dto != null) {
            if (dto.getCategoryId() != null) {
                stringBuilder.append(" AND a.category_id = :categoryId ");
                mapInput.addValue("categoryId", dto.getCategoryId());
            }
            if (org.apache.commons.lang3.StringUtils.isNotBlank(dto.getSeo())) {
                stringBuilder.append(" AND a.seo = :seo ");
                mapInput.addValue("seo", dto.getSeo());
            }
            if (org.apache.commons.lang3.StringUtils.isNotBlank(dto.getKeyword())) {
                stringBuilder.append(" AND (a.title LIKE :keyword OR a.detail_description LIKE :keyword OR a.short_description LIKE :keyword) ");
                mapInput.addValue("keyword", "%" + dto.getKeyword() + "%");
            }
        }

        // Fetch total elements
        StringBuilder countQuery = new StringBuilder();
        countQuery.append(" SELECT COUNT(*) FROM tbl_products a WHERE 1 = 1 ");
        if (dto != null) {
            if (dto.getCategoryId() != null) {
                countQuery.append(" AND a.category_id = :categoryId ");
            }
            if (org.apache.commons.lang3.StringUtils.isNotBlank(dto.getSeo())) {
                countQuery.append(" AND a.seo = :seo ");
            }
            if (org.apache.commons.lang3.StringUtils.isNotBlank(dto.getKeyword())) {
                countQuery.append(" AND (a.title LIKE :keyword OR a.detail_description LIKE :keyword OR a.short_description LIKE :keyword) ");
            }
        }

        Long totalElements = namedParameterJdbcTemplate.queryForObject(countQuery.toString(), mapInput, Long.class);
        if (totalElements == null) {
            totalElements = 0L;
        }

        stringBuilder.append(" ORDER BY a.created_date DESC OFFSET :page_ ROWS FETCH NEXT :size_ ROWS ONLY ");
        mapInput.addValue("page_", pageable.getOffset());
        mapInput.addValue("size_", pageable.getPageSize());

        List<Product> resultList = namedParameterJdbcTemplate.query(stringBuilder.toString(), mapInput, BeanPropertyRowMapper.newInstance(Product.class));
        if (org.apache.commons.collections.CollectionUtils.isNotEmpty(resultList)) {
            return new PageImpl<>(resultList, pageable, totalElements);
        } else {
            return new PageImpl<>(new ArrayList<>(), pageable, 0);
        }
    }

    @Override
    public Product getById(int id) {
        Optional<Product> productOpt = productRepository.findById(id);
        if (productOpt.isPresent()) {
            Product product = productOpt.get();
            List<ProductImage> productImages = productImageRepository.findByProductId(product.getId());
            if (productImages != null && !productImages.isEmpty()){
                product.setProductImages(productImages);
            }
            return product;
        }
        return new Product();
    }

    @Override
    public Product getBySeo(String seo) {
        Product product = productRepository.findBySeo(seo);
        if (product != null){
            List<ProductImage> productImage = productImageRepository.findByProductId(product.getId());
            if (productImage != null && !productImage.isEmpty()){
                product.setProductImages(productImage);
            }
        }
        return product;
    }

    @Override
    @Transactional
    public void add(Product p, MultipartFile productAvatar, MultipartFile[] productPictures)
            throws Exception {

        if (!isEmptyUploadFile(productAvatar)) {
            String avatarUrl = s3Service.uploadFile(productAvatar, "products/avatars");
            p.setAvatar(avatarUrl);
        }
        p.setSeo(new Slugify().slugify(p.getTitle()));
        p.setCreatedDate(new Date());
        Product productNew = productRepository.save(p);

        if (!isEmptyUploadFile(productPictures)) {
            for (MultipartFile pic : productPictures) {
                String picUrl = s3Service.uploadFile(pic, "products/pictures");
                ProductImage pi = new ProductImage();
                pi.setPath(picUrl);
                pi.setTitle(pic.getOriginalFilename());
                pi.setProductId(productNew.getId());
                pi.setCreatedDate(new Date());
                productImageRepository.save(pi);
            }
        }

    }

    @Override
    @Transactional
    public void update(Product p, MultipartFile productAvatar, MultipartFile[] productPictures)
            throws Exception {

        Product productInDb = productRepository.getById(p.getId());
        if (productInDb == null) {
            throw new EntityValidationException("Không tìm thấy sản phẩm");
        }

        if (!isEmptyUploadFile(productAvatar)) {
            String avatarUrl = s3Service.uploadFile(productAvatar, "products/avatars");
            p.setAvatar(avatarUrl);
        } else {
            p.setAvatar(productInDb.getAvatar());
        }

        if (!isEmptyUploadFile(productPictures)) {
            List<ProductImage> productImages = productImageRepository.findByProductId(p.getId());
            if (productImages != null) {
                for (ProductImage opi : productImages) {
                    productImageRepository.delete(opi);
                }
            }

            for (MultipartFile pic : productPictures) {
                String picUrl = s3Service.uploadFile(pic, "products/pictures");
                ProductImage pi = new ProductImage();
                pi.setPath(picUrl);
                pi.setTitle(pic.getOriginalFilename());
                pi.setProductId(p.getId());
                pi.setUpdatedDate(new Date());
                productImageRepository.save(pi);
            }
        }

        p.setSeo(new Slugify().slugify(p.getTitle()));

        productRepository.save(p);
    }

    @Override
    @Transactional
    public void deleteById(int id) throws EntityValidationException{
        List<SaleorderProducts> saleorderProductsList = orderProductRepository.findByProductId(id);
        if(saleorderProductsList != null && !saleorderProductsList.isEmpty()){
            throw new EntityValidationException("Không thể xóa sản phẩm");
        }
        List<ProductImage> productImageList = productImageRepository.findByProductId(id);
        if(productImageList != null && !productImageList.isEmpty()){
            productImageRepository.deleteAllByProductId(id);
        }
        productRepository.deleteById(id);
    }

    @Override
    @Transactional
    public void updateProductStatus(int id, boolean status) throws Exception {
        Product product = productRepository.getById(id);
        if (product == null) {
            throw new EntityValidationException("Không tìm thấy sản phẩm !");
        }
        product.setStatus(status);
        product.setUpdatedDate(new java.util.Date());
        productRepository.save(product);
    }
}
