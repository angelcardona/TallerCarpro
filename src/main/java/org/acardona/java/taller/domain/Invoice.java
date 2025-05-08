package org.acardona.java.taller.domain;

import java.time.LocalDateTime;
import java.util.UUID;

public class Invoice {
    private String id;
    private LocalDateTime date;
    private Client client;
    private Vehicle vehicle;
    private double laborCost;
    private double sparePartsCost;
    private double otherCosts;
    private double total;
    private boolean isVehicleDelivered;
    private PaymentMethod paymentMethod;

    public Invoice() {
    }

    public Invoice(String id, LocalDateTime date, Client client, Vehicle vehicle, double laborCost, double sparePartsCost,
                   double otherCosts, double total, boolean isVehicleDelivered, PaymentMethod paymentMethod) {
        this.id = UUID.randomUUID().toString();
        this.date = date;
        this.client = client;
        this.vehicle = vehicle;
        this.laborCost = laborCost;
        this.sparePartsCost = sparePartsCost;
        this.otherCosts = otherCosts;
        this.total = total;
        this.isVehicleDelivered = isVehicleDelivered;
        this.paymentMethod = paymentMethod;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public Vehicle getVehicle() {
        return vehicle;
    }

    public void setVehicle(Vehicle vehicle) {
        this.vehicle = vehicle;
    }

    public double getLaborCost() {
        return laborCost;
    }

    public void setLaborCost(double laborCost) {
        this.laborCost = laborCost;
    }

    public double getSparePartsCost() {
        return sparePartsCost;
    }

    public void setSparePartsCost(double sparePartsCost) {
        this.sparePartsCost = sparePartsCost;
    }

    public double getOtherCosts() {
        return otherCosts;
    }

    public void setOtherCosts(double otherCosts) {
        this.otherCosts = otherCosts;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public boolean isVehicleDelivered() {
        return isVehicleDelivered;
    }

    public void setVehicleDelivered(boolean vehicleDelivered) {
        isVehicleDelivered = vehicleDelivered;
    }

    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
    }
}
