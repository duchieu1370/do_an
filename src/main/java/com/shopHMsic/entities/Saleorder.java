package com.shopHMsic.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@Table(name = "tbl_saleorder")
public class Saleorder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "status")
    private Integer status;

    @Column(name = "created_by", nullable = true)
    private Integer createdBy;

    @Column(name = "updated_by", nullable = true)
    private Integer updatedBy;

    @Column(name = "updated_date", nullable = true)
    private Date updatedDate;

    @Column(name = "created_date", nullable = true)
    private Date createdDate;

    @Column(name = "code", length = 45, nullable = false)
    private String code;

//	@Column(name = "user_id", nullable = true)
//	private Integer user_id;

    @Column(name = "customer_name", length = 100, nullable = true)
    private String customer_name;

    @Column(name = "customer_address", length = 100, nullable = true)
    private String customer_address;

    @Column(name = "customer_phone", length = 100, nullable = true)
    private String customer_phone;

    @Column(name = "customer_email", length = 100, nullable = true)
    private String customer_email;

    @Column(name = "total", precision = 13, scale = 2, nullable = true)
    private BigDecimal total;

    @Column(name = "order_status", nullable = true)
    private Integer orderStatus = 1;

    @Column(name = "reason", length = 1000, nullable = true)
    private String reason;

    @Column(name = "seo", length = 1000, nullable = true)
    private String seo;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "saleOrder", fetch = FetchType.EAGER)
    private Set<SaleorderProducts> saleOrderProducts = new HashSet<SaleorderProducts>();

    public void addSaleOrderProducts(SaleorderProducts _saleOrderProducts) {
        _saleOrderProducts.setSaleOrder(this);
        saleOrderProducts.add(_saleOrderProducts);
    }

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User user;

    public Set<SaleorderProducts> getSaleOrderProducts() {
        return saleOrderProducts;
    }

    public void setSaleOrderProducts(Set<SaleorderProducts> saleOrderProducts) {
        this.saleOrderProducts = saleOrderProducts;
    }


}
