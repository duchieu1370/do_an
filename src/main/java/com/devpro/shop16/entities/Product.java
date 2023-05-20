package com.devpro.shop16.entities;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;



@Entity
@Table(name="tbl_products") //để spring jpa biết mapping với table nào
public class Product extends BaseEntity{
	
	@Column(name = "title", length = 100, nullable = false)
	private String title;
	
	@Column(name = "price", precision = 13, scale = 2, nullable = false)
	private BigDecimal price;
	
	@Column(name = "price_sale", precision = 13, scale = 2, nullable = true)
	private BigDecimal price_sale;
	
	@Column(name = "short_description", length = 3000, nullable = false)
	private String shortDes;
	
	@Lob
	@Column(name = "detail_description", columnDefinition = "LONGTEXT", nullable = false)
	private String details;
	
	@Column(name = "avatar", nullable = true)
	private String avatar;
	
	@Column(name = "seo", length = 1000, nullable = true)
	private String seo;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "category_id") //định nghĩa khóa ngoại bằng joincolumn
	private Categories categories;
	
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "product", fetch = FetchType.EAGER)
	private Set<ProductImage> productImage = new HashSet<ProductImage>();

	// khi định ngĩa OneToMany thì luôn cần định nghĩa 2 hàm Utility
	public void addProductImage(ProductImage _productImage) {
		_productImage.setProduct(this);// thêm product vào trong Set<Product>
		productImage.add(_productImage);// product phải thuộc đối tượng category đang xét(đang call vào hàm này)
	}

	public void deleteProductImage(ProductImage _productImage) {
		this.productImage.remove(_productImage);// xóa product vào trong Set<Product>
		_productImage.setProduct(null);// product phải xóa khỏi đối tượng category đang xét(đang call vào hàm này)
	}
	

	public Set<ProductImage> getProductImage() {
		return productImage;
	}

	public void setProductImage(Set<ProductImage> productImage) {
		this.productImage = productImage;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public BigDecimal getPrice_sale() {
		return price_sale;
	}

	public void setPrice_sale(BigDecimal price_sale) {
		this.price_sale = price_sale;
	}

	public String getShortDes() {
		return shortDes;
	}

	public void setShortDes(String shortDes) {
		this.shortDes = shortDes;
	}

	public String getDetails() {
		return details;
	}

	public void setDetails(String details) {
		this.details = details;
	}

	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	public String getSeo() {
		return seo;
	}

	public void setSeo(String seo) {
		this.seo = seo;
	}

	public Categories getCategories() {
		return categories;
	}

	public void setCategories(Categories categories) {
		this.categories = categories;
	}
	
	
}
