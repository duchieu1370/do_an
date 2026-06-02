package com.shopHMsic.dto;

public class UserSearchModel extends BaseSearchModel {

	// tìm theo keyword
	public String keyword;

	// tìm theo category
	public Integer roleId;

	public Integer getRoleId() {
		return roleId;
	}

	public String getKeyword() {
		return keyword;
	}

}
