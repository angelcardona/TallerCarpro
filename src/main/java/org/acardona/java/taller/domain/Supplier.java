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
}
