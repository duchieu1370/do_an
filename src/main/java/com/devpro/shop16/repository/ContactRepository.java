package com.devpro.shop16.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.devpro.shop16.entities.Contact;

import java.util.List;

@Repository
public interface ContactRepository extends JpaRepository<Contact, Integer>{
    @Query("select a from Contact as a where a.email = :email")
    List<Contact> findByEmailContact(@Param("email") String email);
}
