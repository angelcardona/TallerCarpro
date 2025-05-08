package org.acardona.java.taller.domain;

import java.time.LocalDateTime;
import java.util.UUID;

public class SupplierInvoice {
    private String id;
    private LocalDateTime date;
    private Supplier supplier;
    private Vehicle vehicle;
    private double total;
    private String description;
    private boolean paid;

    public SupplierInvoice() {
    }

    public SupplierInvoice(String id, String description, double total, Vehicle vehicle, Supplier supplier, LocalDateTime date) {
        this.id = UUID.randomUUID().toString();
        this.paid = false;
        this.description = description;
        this.total = total;
        this.vehicle = vehicle;
        this.supplier = supplier;
        this.date = date;
    }

    public SupplierInvoice(LocalDateTime date, Supplier supplier, Vehicle vehicle, double total, String description) {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isPaid() {
        return paid;
    }

    public void setPaid(boolean paid) {
        this.paid = paid;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public Vehicle getVehicle() {
        return vehicle;
    }

    public void setVehicle(Vehicle vehicle) {
        this.vehicle = vehicle;
    }

    public Supplier getSupplier() {
        return supplier;
    }

    public void setSupplier(Supplier supplier) {
        this.supplier = supplier;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }
}
