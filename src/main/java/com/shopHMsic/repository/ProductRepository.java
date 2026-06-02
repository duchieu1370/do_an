package com.shopHMsic.repository;

import com.shopHMsic.dto.ProductSearchDataModel;
import com.shopHMsic.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer>{

    @PersistenceContext //Inject entityManager
    EntityManager entityManager = null;

    @PersistenceContext
    default Class<ProductSearchDataModel> clazz() {
        // TODO Auto-generated method stub
        return ProductSearchDataModel.class;
    }

}
