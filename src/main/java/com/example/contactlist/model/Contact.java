package com.example.contactlist.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "contact")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
//@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class Contact {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)

    @Column(name = "contact_id", unique = true, updatable = false)
    public String id;
    private String name;
    private String email;
    private String title;
    private String phone;
    private String address;
    private String status;
    private String photoUrl;
}
