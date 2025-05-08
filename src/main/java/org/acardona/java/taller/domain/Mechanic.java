package org.acardona.java.taller.domain;

import java.util.UUID;

public class Mechanic {
    private  String id;
    private String name;
    private String phone;
    private double weeklyPayment;

    public Mechanic() {
    }

    public Mechanic(String id, double weeklyPayment, String phone, String name) {
        this.id = UUID.randomUUID().toString();
        this.weeklyPayment = weeklyPayment;
        this.phone = phone;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public double getWeeklyPayment() {
        return weeklyPayment;
    }

    public void setWeeklyPayment(double weeklyPayment) {
        this.weeklyPayment = weeklyPayment;
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
