package com.jai.croop.model;

import java.sql.Date;
import java.time.LocalDate;

public class DailySalesDTO {
    private LocalDate date;
    private double totalSale;

    public DailySalesDTO() {}


    public DailySalesDTO(LocalDate date, double totalSale) {
        this.date = date;
        this.totalSale = totalSale;
    }

    // PostgreSQL-specific constructor
    public DailySalesDTO(Date sqlDate, Double totalSale) {
        this.date = sqlDate.toLocalDate();
        this.totalSale = totalSale;
    }

    // Getters and setters
    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }
    public double getTotalSale() { return totalSale; }
    public void setTotalSale(double totalSale) { this.totalSale = totalSale; }
}