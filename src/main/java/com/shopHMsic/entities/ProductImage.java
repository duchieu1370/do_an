package com.shopHMsic.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "tbl_products_images")
public class ProductImage extends BaseEntity {
    @Column(name = "title", length = 500, nullable = false)
    private String title;

    @Column(name = "path", length = 200, nullable = false)
    private String path;

    @Column(name = "product_id")
    private Integer productId;


}
