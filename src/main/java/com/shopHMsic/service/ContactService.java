package com.shopHMsic.service;

import com.shopHMsic.dto.ContactSearchModel;
import com.shopHMsic.entities.Contact;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ContactService {
    Page<Contact> getListContact(ContactSearchModel dto, Pageable pageable);
    
    Contact getById(int id);
    
    void deleteContact(int id) throws Exception;
    
    void updateContactStatus(int id, boolean status) throws Exception;

    // Compatibility method for existing controllers
    Contact saveOrUpdate(Contact contact);
}
