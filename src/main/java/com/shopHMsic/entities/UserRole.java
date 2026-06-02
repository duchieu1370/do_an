package com.shopHMsic.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name="TBL_USERS_ROLES")
public class UserRole extends BaseEntity{

	@Column(name = "ROLE_ID")
	private Integer roleId;
	
	@Column(name = "USER_ID")
	private Integer userId;
	



}
