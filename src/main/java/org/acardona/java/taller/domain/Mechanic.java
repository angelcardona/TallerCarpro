package org.acardona.java.taller.domain;

import java.util.UUID;

public class Mechanic {
    private  String id;
    private String name;
    private String phone;


    public Mechanic() {
    }

    public Mechanic(String id, String phone, String name) {
        this.id = UUID.randomUUID().toString();

        this.phone = phone;
        this.name = name;
    }

    public Mechanic(String name, String phone) {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
