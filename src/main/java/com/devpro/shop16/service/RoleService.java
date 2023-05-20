package com.devpro.shop16.service;

import java.util.List;

import com.devpro.shop16.entities.Role;
import com.devpro.shop16.entities.User;

public class RoleService extends BaseService<Role>{

	@Override
	protected Class<Role> clazz() {
		// TODO Auto-generated method stub
		return Role.class;
	}
	
	public Role loadRoleByRolename(String roleName) {
		String sql = "select * from tbl_roles u where u.username = '" + roleName + "'";
		List<Role> role = this.executeByNativeSQL(sql, 0).getData();

		if (role == null || role.size() <= 0)
			return null;
		return role.get(0);
	}
	
	

}
