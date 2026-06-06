package com.shopHMsic.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@Table(name = "tbl_users")
public class User extends BaseEntity implements UserDetails {
    @Column(name = "username", length = 100, nullable = false, unique = true)
    private String username;

    @Column(name = "password", length = 100, nullable = false)
    private String password;

    @Column(name = "email", length = 45, nullable = false, unique = true)
    private String email;

    @Column(name = "address", length = 1000)
    private String address;

    @Column(name = "phone", length = 10)
    private String phone;

    @Column(name = "avatar", length = 500)
    private String avatar;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "users")
    private Set<Role> roles = new HashSet<Role>();

    public void addRoles(Role role) {
        role.getUsers().add(this);
        roles.add(role);
    }

    public void deleteRoles(Role role) {
        role.getUsers().remove(this);
        roles.remove(role);
    }

    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "user")
    private Set<Saleorder> saleOrders = new HashSet<Saleorder>();

    public void addSaleOrder(Saleorder saleOrder) {
        saleOrders.add(saleOrder);
        saleOrder.setUser(this);
    }

    public void deleteSaleOrder(Saleorder saleOrder) {
        saleOrders.remove(saleOrder);
        saleOrder.setUser(null);
    }

    public Set<Saleorder> getSaleOrders() {
        return saleOrders;
    }

    public void setSaleOrders(Set<Saleorder> saleOrders) {
        this.saleOrders = saleOrders;
    }


    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // TODO Auto-generated method stub
        return roles;
    }

    @Override
    public boolean isAccountNonExpired() {
        // TODO Auto-generated method stub
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        // TODO Auto-generated method stub
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        // TODO Auto-generated method stub
        return true;
    }

    @Override
    public boolean isEnabled() {
        return getStatus() != null ? getStatus() : false;
    }

}
