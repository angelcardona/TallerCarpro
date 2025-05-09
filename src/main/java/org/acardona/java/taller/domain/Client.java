package org.acardona.java.taller.domain;

import java.util.UUID;

public class Client {
    private String id;
    private String name;
    private String email;
    private String phone;
    private String identification;

    public Client(String name, String email, String phone, String identification) {
    }

    public Client(String id, String identification, String phone, String email, String name) {
        this.id = UUID.randomUUID().toString();
        this.identification = identification;
        this.phone = phone;
        this.email = email;
        this.name = name;
    }

    public Client(String name, String contact, String email) {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIdentification() {
        return identification;
    }

    public void setIdentification(String identification) {
        this.identification = identification;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
