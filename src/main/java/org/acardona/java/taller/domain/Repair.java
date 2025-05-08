package org.acardona.java.taller.domain;

public class Repair {
    private String id;
    private String repairType;
    private String description;
    private double laborCost;
    private Mechanic mechanic;
    private String status;
    private Vehicle vehicle;

    public Repair() {
    }

    public Repair(String id, Vehicle vehicle, String status, Mechanic mechanic, double laborCost, String description, String repairType) {
        this.id = id;
        this.vehicle = vehicle;
        this.status = status;
        this.mechanic = mechanic;
        this.laborCost = laborCost;
        this.description = description;
        this.repairType = repairType;
    }

}