package org.acardona.java.taller.domain;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.Optional;

public class Repair {
    private String id;
    private String repairType;
    private String description;
    private double laborCost;
    private Mechanic mechanic;
    private String status;
    private Vehicle vehicle;
    private double mechanic_labor_percentage;
    private Date start_date;

    public Repair() {
    }

    public Repair(String id, String repairType, String description, double laborCost, Mechanic mechanic,
                  String status, Vehicle vehicle, double mechanic_labor_percentage) {
        this.id = id;
        this.repairType = repairType;
        this.description = description;
        this.laborCost = laborCost;
        this.mechanic = mechanic;
        this.status = status;
        this.vehicle = vehicle;
        this.mechanic_labor_percentage = mechanic_labor_percentage;
    }

    public Repair(String repairType, String description, double laborCost, double mechanicLaborPercentage, String status, Optional<Vehicle> vehicle, Mechanic mechanic) {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRepairType() {
        return repairType;
    }

    public void setRepairType(String repairType) {
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
        this.laborCost = laborCost;
    }

    public Mechanic getMechanic() {
        return mechanic;
    }

    public void setMechanic(Mechanic mechanic) {
        this.mechanic = mechanic;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Vehicle getVehicle() {
        return vehicle;
    }

    public void setVehicle(Vehicle vehicle) {
        this.vehicle = vehicle;
    }
    public double getMechanic_labor_percentage() {
        return mechanic_labor_percentage;
    }

    public void setMechanic_labor_percentage(double mechanic_labor_percentage) {
        this.mechanic_labor_percentage = mechanic_labor_percentage;
    }

    public Date getStart_date() {
        return start_date;
    }

    public void setStart_date(LocalDateTime start_date) {
        this.start_date = start_date;
    }

    public Iterable<? extends SparePart> getSpareParts() {
        return null;
    }
}