package com.shopHMsic.entities;

import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;

@MappedSuperclass
@Getter
@Setter
public abstract class BaseEntity {
	@Id //primary key
	@GeneratedValue(strategy = GenerationType.IDENTITY)//auto increment
	@Column(name = "id")//column name
	private Integer id;
	
	@Column(name = "status", nullable = true)//column name
	private Boolean status = Boolean.TRUE;
	
	@Column(name = "created_by", nullable = true)
	private Integer createdBy;
	
	@Column(name = "updated_by", nullable = true)
	private Integer updatedBy;
	
	@Column(name = "updated_date", nullable = true)
	private Date updatedDate;
	
	@Column(name = "created_date", nullable = true)
	private Date createdDate;

}
