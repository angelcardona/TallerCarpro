package org.acardona.java.taller.domain;

import java.util.UUID;

public class Vehicle {
    private String id;
    private String brand;
    private String model;
    private int year;
    private String license_plate;
    private String type;
    private Client client;

    public Vehicle() {
    }

    public Vehicle(String id, Client client, String type, String license_plate, int year, String model, String brand) {
        this.id = UUID.randomUUID().toString();
        this.client = client;
        this.type = type;
        this.license_plate = license_plate;
        this.year = year;
        this.model = model;
        this.brand = brand;
    }

    public Vehicle(String brand, String model, String year, String licensePlate, String type, Client client) {
    }


    public Vehicle(String brand, String model, int year, String licensePlate, String type, Object client) {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getLicense_plate() {
        return license_plate;
    }

    public void setLicense_plate(String license_plate) {
        this.license_plate = license_plate;
    }

    public String getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }
}
