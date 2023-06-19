package com.devpro.shop16.service;

import com.devpro.shop16.entities.Contact;
import com.devpro.shop16.repository.ContactRepository;
import org.springframework.stereotype.Service;

@Service
public class ContactServiceImpl implements IContactService{
    private final ContactRepository contactRepository;

    public ContactServiceImpl(ContactRepository contactRepository, ContactService contactService) {
        this.contactRepository = contactRepository;
        this.contactService = contactService;
    }

    private final ContactService contactService;

    @Override
    public boolean deleteContact(Integer id) {
        if(id>=1){
            Contact contact = contactService.getById(id);
            if(contact!=null){
                contactRepository.delete(contact);
                return true;
            }
        }
        return false;
    }
}
