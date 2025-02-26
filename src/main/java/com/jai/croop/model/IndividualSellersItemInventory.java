package com.jai.croop.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;

@Entity
@Table(name = "individual_sellers_items")
public class IndividualSellersItemInventory extends BaseInventory{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @ManyToOne
    @JoinColumn(name = "individual_seller_id", referencedColumnName = "id", nullable = false)
    @JsonBackReference("individual-inventory")
    private IndividualSellers individualSellers;

    public IndividualSellersItemInventory(){

    }

    public IndividualSellersItemInventory(IndividualSellers individualSellers, String itemName, int itemStart, int itemUsed,
                                     int itemRemaining){
        super(itemName, itemStart, itemUsed,itemRemaining);
        this.individualSellers = individualSellers;
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
}
