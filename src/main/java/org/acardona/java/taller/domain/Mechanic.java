package org.acardona.java.taller.domain;

import java.util.UUID;

public class Mechanic {
    private String id;
    private String name;
    private String phone;
    private double weeklyPayment;

    public Mechanic(String name, String phone) {
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("Name cannot be null or empty");
        }
        this.id = UUID.randomUUID().toString();
        this.name = name;
        this.phone = phone;
        this.weeklyPayment = 0.0;
    }

    // Constructor para cargar desde la base de datos
    public Mechanic(String id, String name, String phone, double weeklyPayment) {
        if (id == null || id.isEmpty()) {
            throw new IllegalArgumentException("ID cannot be null or empty");
        }
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("Name cannot be null or empty");
        }
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.weeklyPayment = weeklyPayment;
    }

    // Getters y setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        if (id == null || id.isEmpty()) {
            throw new IllegalArgumentException("ID cannot be null or empty");
        }
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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public double getWeeklyPayment() {
        return weeklyPayment;
    }

    public void setWeeklyPayment(double weeklyPayment) {
        this.weeklyPayment = weeklyPayment;
    }
}