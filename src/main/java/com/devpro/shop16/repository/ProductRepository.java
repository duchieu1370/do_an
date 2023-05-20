package com.devpro.shop16.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.devpro.shop16.entities.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer>{

}
