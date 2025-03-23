package com.jai.croop.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "individual_sellers_products")
public class IndividualSellersProductsInventory extends BaseInventory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "shelf_life_days")
    private int shelfLifeDays;
    private LocalDate localDate;
    @ManyToOne
    @JoinColumn(name = "individual_seller_id", referencedColumnName = "id", nullable = false)
    @JsonBackReference("individual-products")
    private IndividualSellers individualSellers;

    @ManyToOne
    @JsonManagedReference("individual-products-discounted")
    private IndividualSellersDiscount individualSellersDiscount;


    private int price;
    public IndividualSellersProductsInventory(){

    }

    public IndividualSellersProductsInventory(IndividualSellers individualSellers, IndividualSellersDiscount individualSellersDiscount, String itemName, int itemStart, int itemUsed,
                                         int itemRemaining, String unit, int shelfLifeDays, LocalDate localDate){
        super(itemName, itemStart, itemUsed,itemRemaining,unit);
        this.individualSellers = individualSellers;
        this.individualSellersDiscount = individualSellersDiscount;
        this.shelfLifeDays = shelfLifeDays;
        this.localDate = localDate;
    }

    public int getId() {
        return id;
    }

    public IndividualSellers getIndividualSellers() {
        return individualSellers;
    }

    public void setIndividualSellers(IndividualSellers individualSellers) {
        this.individualSellers = individualSellers;
    }

    public void setPrice(int price) {
        this.price = price;
    }
    public int getPrice() {
        return price;
    }

    public IndividualSellersDiscount getIndividualSellersDiscount() {
        return individualSellersDiscount;
    }
    public void setIndividualSellersDiscount(IndividualSellersDiscount individualSellersDiscount) {
        this.individualSellersDiscount = individualSellersDiscount;
    }

    public int getShelfLifeDays() {
        return shelfLifeDays;
    }
    public void setShelfLifeDays(int shelfLifeDays) {
        this.shelfLifeDays = shelfLifeDays;
    }

    public LocalDate getLocalDate() {
        return localDate;
    }
    public void setLocalDate(LocalDate localDate) {
        this.localDate = localDate;
    }
}
