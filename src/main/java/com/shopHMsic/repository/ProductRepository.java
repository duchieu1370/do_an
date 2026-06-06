package com.shopHMsic.repository;

import com.shopHMsic.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {

    Product findBySeo(String seo);

}
