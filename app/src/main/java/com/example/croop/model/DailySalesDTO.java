package com.example.croop.model;

import java.time.LocalDate;

public class DailySalesDTO {
    private LocalDate date;
    private double totalSale;

    public DailySalesDTO(){

    }

    public DailySalesDTO(LocalDate date,double totalSale){
        this.date = date;
        this.totalSale = totalSale;
    }

    public LocalDate getDate() {
        return date;
    }
    public void setDate(LocalDate date) {
        this.date = date;
    }

    public double getTotalSale() {
        return totalSale;
    }
    public void setTotalSale(double totalSale) {
        this.totalSale = totalSale;
    }
}