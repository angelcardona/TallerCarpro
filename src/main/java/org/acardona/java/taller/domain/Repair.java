package org.acardona.java.taller.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Repair {
    private String id;
    private String repairType;
    private String description;
    private double laborCost;
    private Mechanic mechanic;
    private String status;
    private Vehicle vehicle;
    private double mechanicLaborPercentage;
    private LocalDateTime startDate;
    private List<SparePart> spareParts;

    public Repair(String repairType, String description, double laborCost, double mechanicLaborPercentage,
                  String status, Vehicle vehicle, Mechanic mechanic, LocalDateTime startDate) {
        if (repairType == null || repairType.isEmpty()) {
            throw new IllegalArgumentException("Repair type cannot be null or empty");
        }
        if (laborCost < 0) {
            throw new IllegalArgumentException("Labor cost cannot be negative");
        }
        if (mechanicLaborPercentage < 0 || mechanicLaborPercentage > 100) {
            throw new IllegalArgumentException("Mechanic labor percentage must be between 0 and 100");
        }
        if (status == null || status.isEmpty()) {
            throw new IllegalArgumentException("Status cannot be null or empty");
        }
        if (vehicle == null) {
            throw new IllegalArgumentException("Vehicle cannot be null");
        }
        if (mechanic == null) {
            throw new IllegalArgumentException("Mechanic cannot be null");
        }
        if (startDate == null) {
            throw new IllegalArgumentException("Start date cannot be null");
        }
        this.id = UUID.randomUUID().toString();
        this.repairType = repairType;
        this.description = description;
        this.laborCost = laborCost;
        this.mechanicLaborPercentage = mechanicLaborPercentage;
        this.status = status;
        this.vehicle = vehicle;
        this.mechanic = mechanic;
        this.startDate = startDate;
        this.spareParts = new ArrayList<>();
    }

    // Constructor para cargar desde la base de datos
    public Repair(String id, String repairType, String description, double laborCost, double mechanicLaborPercentage,
                  String status, Vehicle vehicle, Mechanic mechanic, LocalDateTime startDate) {
        if (id == null || id.isEmpty()) {
            throw new IllegalArgumentException("ID cannot be null or empty");
        }
        if (repairType == null || repairType.isEmpty()) {
            throw new IllegalArgumentException("Repair type cannot be null or empty");
        }
        if (laborCost < 0) {
            throw new IllegalArgumentException("Labor cost cannot be negative");
        }
        if (mechanicLaborPercentage < 0 || mechanicLaborPercentage > 100) {
            throw new IllegalArgumentException("Mechanic labor percentage must be between 0 and 100");
        }
        if (status == null || status.isEmpty()) {
            throw new IllegalArgumentException("Status cannot be null or empty");
        }
        if (vehicle == null) {
            throw new IllegalArgumentException("Vehicle cannot be null");
        }
        if (mechanic == null) {
            throw new IllegalArgumentException("Mechanic cannot be null");
        }
        if (startDate == null) {
            throw new IllegalArgumentException("Start date cannot be null");
        }
        this.id = id;
        this.repairType = repairType;
        this.description = description;
        this.laborCost = laborCost;
        this.mechanicLaborPercentage = mechanicLaborPercentage;
        this.status = status;
        this.vehicle = vehicle;
        this.mechanic = mechanic;
        this.startDate = startDate;
        this.spareParts = new ArrayList<>();
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

    public String getRepairType() {
        return repairType;
    }

    public void setRepairType(String repairType) {
        if (repairType == null || repairType.isEmpty()) {
            throw new IllegalArgumentException("Repair type cannot be null or empty");
        }
        this.repairType = repairType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getLaborCost() {
        return laborCost;
    }

    public void setLaborCost(double laborCost) {
        if (laborCost < 0) {
            throw new IllegalArgumentException("Labor cost cannot be negative");
        }
        this.laborCost = laborCost;
    }

    public Mechanic getMechanic() {
        return mechanic;
    }

    public void setMechanic(Mechanic mechanic) {
        if (mechanic == null) {
            throw new IllegalArgumentException("Mechanic cannot be null");
        }
        this.mechanic = mechanic;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        if (status == null || status.isEmpty()) {
            throw new IllegalArgumentException("Status cannot be null or empty");
        }
        this.status = status;
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

    public double getMechanicLaborPercentage() {
        return mechanicLaborPercentage;
    }

    public void setMechanicLaborPercentage(double mechanicLaborPercentage) {
        if (mechanicLaborPercentage < 0 || mechanicLaborPercentage > 100) {
            throw new IllegalArgumentException("Mechanic labor percentage must be between 0 and 100");
        }
        this.mechanicLaborPercentage = mechanicLaborPercentage;
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDateTime startDate) {
        if (startDate == null) {
            throw new IllegalArgumentException("Start date cannot be null");
        }
        this.startDate = startDate;
    }

    public List<SparePart> getSpareParts() {
        return spareParts;
    }

    public void addSparePart(SparePart sparePart) {
        if (sparePart == null) {
            throw new IllegalArgumentException("Spare part cannot be null");
        }
        this.spareParts.add(sparePart);
    }
}