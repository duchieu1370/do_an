package com.shopHMsic.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;


@Entity
@Getter
@Setter
@Table(name = "tbl_products")
public class Product extends BaseEntity {

    @Column(name = "title", length = 100, nullable = false)
    private String title;

    @Column(name = "price", precision = 13, scale = 2, nullable = false)
    private BigDecimal price;

    @Column(name = "price_sale", precision = 13, scale = 2, nullable = true)
    private BigDecimal priceSale;

    @Column(name = "short_description", length = 3000, nullable = false)
    private String shortDescription;

    @Lob
    @Column(name = "detail_description", nullable = false)
    private String detailDescription;

    @Column(name = "avatar", nullable = true)
    private String avatar;

    @Column(name = "seo", length = 1000, nullable = true)
    private String seo;

    @Column(name = "category_id") //định nghĩa khóa ngoại bằng joincolumn
    private Integer categoryId;

    @Column(name = "color", length = 255, nullable = true)
    private String color;

    @Transient
    private List<ProductImage> productImages;

}
