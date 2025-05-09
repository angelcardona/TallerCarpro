package org.acardona.java.taller.domain;

import java.util.UUID;

public class Client {
    private String id;
    private String name;
    private String email;
    private String phone;
    private String identification;

    public Client(String name, String email, String phone, String identification) {
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("Name cannot be null or empty");
        }
        if (identification == null || identification.isEmpty()) {
            throw new IllegalArgumentException("Identification cannot be null or empty");
        }
        this.id = UUID.randomUUID().toString();
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.identification = identification;
    }

    // Constructor para cargar desde la base de datos
    public Client(String id, String name, String email, String phone, String identification) {
        if (id == null || id.isEmpty()) {
            throw new IllegalArgumentException("ID cannot be null or empty");
        }
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("Name cannot be null or empty");
        }
        if (identification == null || identification.isEmpty()) {
            throw new IllegalArgumentException("Identification cannot be null or empty");
        }
        this.id = id;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.identification = identification;
    }

    // Getters y setters
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
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("Name cannot be null or empty");
        }
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getIdentification() {
        return identification;
    }

    public void setIdentification(String identification) {
        if (identification == null || identification.isEmpty()) {
            throw new IllegalArgumentException("Identification cannot be null or empty");
        }
        this.identification = identification;
    }

}