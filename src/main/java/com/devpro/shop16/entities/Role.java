package com.devpro.shop16.entities;

import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "tbl_roles") // để spring jpa biết mapping với table nào
public class Role extends BaseEntity implements GrantedAuthority{
	@Column(name = "name", length = 100, nullable = false)
	private String name;

	@Column(name = "description", length = 45, nullable = false)
	private String description;

	@ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinTable(name = "tbl_users_roles", joinColumns = @JoinColumn(name = "role_id")
			, inverseJoinColumns = @JoinColumn(name = "user_id"))
	private Set<User> users = new HashSet<User>();

	public void addUsers(User user) {
		user.getRoles().add(this);
		users.add(user);
	}

	public void deleteUsers(User user) {
		user.getRoles().remove(this);
		users.remove(user);
	}

	public Set<User> getUsers() {
		return users;
	}

	public void setUsers(Set<User> users) {
		this.users = users;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public String getAuthority() {
		// TODO Auto-generated method stub
		return name;
	}

}
