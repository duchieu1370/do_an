package com.shopHMsic.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@Table(name = "tbl_category")
public class Categories extends BaseEntity {

    @Column(name = "name", length = 100, nullable = false)
    private String name;

    @Column(name = "description", length = 100, nullable = false)
    private String description;

    @Column(name = "seo", length = 1000, nullable = true)
    private String seo;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "parent_id")
    private Categories parent;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "parent")
    private Set<Categories> childs = new HashSet<Categories>();

}
