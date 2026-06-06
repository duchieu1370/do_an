package com.shopHMsic.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "tbl_products_ratings", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"product_id", "user_id"})
})
@Getter
@Setter
public class ProductRating extends BaseEntity {

    @Column(name = "rating", nullable = false)
    private Integer rating;

    @Column(name = "user_comment", length = 2000, nullable = true)
    private String comment;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

}
