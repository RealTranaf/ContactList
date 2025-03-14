package com.example.contactlist.repository;

import com.example.contactlist.model.Contact;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ContactRepo extends JpaRepository<Contact,Long> {
    Optional<Contact> findById(String id);

    void deleteById(String id);
}
