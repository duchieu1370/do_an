package com.devpro.shop16.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;

import com.devpro.shop16.entities.Contact;

@Repository
public interface ContactRepository extends JpaRepository<Contact, Integer>{

}
