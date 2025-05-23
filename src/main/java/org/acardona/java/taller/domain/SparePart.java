package org.acardona.java.taller.domain;

public class SparePart {
    private String id;
    private String name;
    private String description;
    private double cost;
    private double profitPercentage;
    private SupplierInvoice supplierInvoice;

    public SparePart() {
    }

    public SparePart(String id, double profitPercentage, double cost, String name, String description,SupplierInvoice supplierInvoice) {
        this.id = id;
        this.profitPercentage = profitPercentage;
        this.cost = cost;
        this.name = name;
        this.description = description;
        this.supplierInvoice=supplierInvoice;
    }

    public SparePart(String name, String description, double profitPercentage, SupplierInvoice supplierInvoice) {

    }

    public SparePart(String name, String description, double cost, double profitPercentage, SupplierInvoice supplierInvoice) {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public double getProfitPercentage() {
        return profitPercentage;
    }

    public void setProfitPercentage(double profitPercentage) {
        this.profitPercentage = profitPercentage;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public SupplierInvoice getSupplierInvoice() {
        return supplierInvoice;
    }

    public void setSupplierInvoice(SupplierInvoice supplierInvoice) {
        this.supplierInvoice = supplierInvoice;
    }
}
