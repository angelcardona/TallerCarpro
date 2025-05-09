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

    public Invoice(LocalDateTime date, Client client, Vehicle vehicle, double laborCost, double sparePartsCost,
                   double otherCosts, PaymentMethod paymentMethod) {
        if (date == null) {
            throw new IllegalArgumentException("Date cannot be null");
        }
        if (client == null) {
            throw new IllegalArgumentException("Client cannot be null");
        }
        if (vehicle == null) {
            throw new IllegalArgumentException("Vehicle cannot be null");
        }
        if (paymentMethod == null) {
            throw new IllegalArgumentException("Payment method cannot be null");
        }
        if (laborCost < 0 || sparePartsCost < 0 || otherCosts < 0) {
            throw new IllegalArgumentException("Costs cannot be negative");
        }
        this.id = UUID.randomUUID().toString();
        this.date = date;
        this.client = client;
        this.vehicle = vehicle;
        this.laborCost = laborCost;
        this.sparePartsCost = sparePartsCost;
        this.otherCosts = otherCosts;
        this.total = laborCost + sparePartsCost + otherCosts;
        this.isVehicleDelivered = false;
        this.paymentMethod = paymentMethod;
    }

    // Constructor para cargar desde la base de datos
    public Invoice(String id, LocalDateTime date, Client client, Vehicle vehicle, double laborCost, double sparePartsCost,
                   double otherCosts, double total, boolean isVehicleDelivered, PaymentMethod paymentMethod) {
        if (id == null || id.isEmpty()) {
            throw new IllegalArgumentException("ID cannot be null or empty");
        }
        if (date == null) {
            throw new IllegalArgumentException("Date cannot be null");
        }
        if (client == null) {
            throw new IllegalArgumentException("Client cannot be null");
        }
        if (vehicle == null) {
            throw new IllegalArgumentException("Vehicle cannot be null");
        }
        if (paymentMethod == null) {
            throw new IllegalArgumentException("Payment method cannot be null");
        }
        if (laborCost < 0 || sparePartsCost < 0 || otherCosts < 0 || total < 0) {
            throw new IllegalArgumentException("Costs and total cannot be negative");
        }
        this.id = id;
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

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        if (date == null) {
            throw new IllegalArgumentException("Date cannot be null");
        }
        this.date = date;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        if (client == null) {
            throw new IllegalArgumentException("Client cannot be null");
        }
        this.client = client;
    }

    public Vehicle getVehicle() {
        return vehicle;
    }

    public void setVehicle(Vehicle vehicle) {
        if (vehicle == null) {
            throw new IllegalArgumentException("Vehicle cannot be null");
        }
        this.vehicle = vehicle;
    }

    public double getLaborCost() {
        return laborCost;
    }

    public void setLaborCost(double laborCost) {
        if (laborCost < 0) {
            throw new IllegalArgumentException("Labor cost cannot be negative");
        }
        this.laborCost = laborCost;
        updateTotal();
    }

    public double getSparePartsCost() {
        return sparePartsCost;
    }

    public void setSparePartsCost(double sparePartsCost) {
        if (sparePartsCost < 0) {
            throw new IllegalArgumentException("Spare parts cost cannot be negative");
        }
        this.sparePartsCost = sparePartsCost;
        updateTotal();
    }

    public double getOtherCosts() {
        return otherCosts;
    }

    public void setOtherCosts(double otherCosts) {
        if (otherCosts < 0) {
            throw new IllegalArgumentException("Other costs cannot be negative");
        }
        this.otherCosts = otherCosts;
        updateTotal();
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        if (total < 0) {
            throw new IllegalArgumentException("Total cannot be negative");
        }
        this.total = total;
    }

    public boolean isVehicleDelivered() {
        return isVehicleDelivered;
    }

    public void setVehicleDelivered(boolean vehicleDelivered) {
        this.isVehicleDelivered = vehicleDelivered;
    }

    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(PaymentMethod paymentMethod) {
        if (paymentMethod == null) {
            throw new IllegalArgumentException("Payment method cannot be null");
        }
        this.paymentMethod = paymentMethod;
    }

    private void updateTotal() {
        this.total = laborCost + sparePartsCost + otherCosts;
    }
}