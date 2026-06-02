package com.shopHMsic.entities;

import jakarta.persistence.*;
import java.util.Date;

@Entity
@Table(name = "tbl_tokens")
public class Token extends BaseEntity {

    @Column(name = "token", length = 2000, nullable = false, unique = true)
    private String token;

    @Column(name = "expiry_date", nullable = false)
    private Date expiryDate;

    @Column(name = "is_revoked", nullable = false)
    private Boolean revoked = false;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Date getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(Date expiryDate) {
        this.expiryDate = expiryDate;
    }

    public Boolean getRevoked() {
        return revoked;
    }

    public void setRevoked(Boolean revoked) {
        this.revoked = revoked;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
