package com.devpro.shop16.repository;

import com.devpro.shop16.dto.ProductSearchDataModel;
import com.devpro.shop16.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

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
