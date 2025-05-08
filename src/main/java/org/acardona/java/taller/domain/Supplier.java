package org.acardona.java.taller.domain;

import java.util.UUID;

public class Supplier {
    private String id;
    private String name;
    private String contact;
    private String email;

    public Supplier() {
    }

    public Supplier(String id, String name, String contact, String email) {
        this.id = UUID.randomUUID().toString();
        this.name = name;
        this.contact = contact;
        this.email = email;
    }


    public Supplier(String supplierName, String contact, String email) {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
