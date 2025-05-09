package org.acardona.java.taller.domain;

import java.time.LocalDateTime;
import java.util.UUID;

public class WeeklyBalance {
    private String id;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private double totalIncome;
    private double cashIncome;
    private double cardIncome;
    private double transferIncome;
    private double totalExpenses;
    private double totalSparePartsProfit;
    private double totalLaborProfit;
    private double balance;

    public WeeklyBalance(LocalDateTime startDate, LocalDateTime endDate, double totalIncome,
                         double cashIncome, double cardIncome, double transferIncome,
                         double totalExpenses, double totalSparePartsProfit, double totalLaborProfit) {
        this.id = UUID.randomUUID().toString();
        this.startDate = startDate;
        this.endDate = endDate;
        this.totalIncome = totalIncome;
        this.cashIncome = cashIncome;
        this.cardIncome = cardIncome;
        this.transferIncome = transferIncome;
        this.totalExpenses = totalExpenses;
        this.totalSparePartsProfit = totalSparePartsProfit;
        this.totalLaborProfit = totalLaborProfit;
        this.balance = totalIncome - totalExpenses;
    }

    // Getters y setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public LocalDateTime getStartDate() { return startDate; }
    public void setStartDate(LocalDateTime startDate) { this.startDate = startDate; }
    public LocalDateTime getEndDate() { return endDate; }
    public void setEndDate(LocalDateTime endDate) { this.endDate = endDate; }
    public double getTotalIncome() { return totalIncome; }
    public void setTotalIncome(double totalIncome) { this.totalIncome = totalIncome; }
    public double getCashIncome() { return cashIncome; }
    public void setCashIncome(double cashIncome) { this.cashIncome = cashIncome; }
    public double getCardIncome() { return cardIncome; }
    public void setCardIncome(double cardIncome) { this.cardIncome = cardIncome; }
    public double getTransferIncome() { return transferIncome; }
    public void setTransferIncome(double transferIncome) { this.transferIncome = transferIncome; }
    public double getTotalExpenses() { return totalExpenses; }
    public void setTotalExpenses(double totalExpenses) { this.totalExpenses = totalExpenses; }
    public double getTotalSparePartsProfit() { return totalSparePartsProfit; }
    public void setTotalSparePartsProfit(double totalSparePartsProfit) { this.totalSparePartsProfit = totalSparePartsProfit; }
    public double getTotalLaborProfit() { return totalLaborProfit; }
    public void setTotalLaborProfit(double totalLaborProfit) { this.totalLaborProfit = totalLaborProfit; }
    public double getBalance() { return balance; }
    public void setBalance(double balance) { this.balance = balance; }
}